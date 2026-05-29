package com.jinyue.cixiu.common.embedding;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.json.JsonData;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Elasticsearch 向量存储实现
 */
@Slf4j
public class ElasticsearchEmbeddingStoreImpl implements EmbeddingStore<TextSegment> {

    private final ElasticsearchClient client;
    private final String indexName;
    private final int dimension;

    public ElasticsearchEmbeddingStoreImpl(ElasticsearchClient client, String indexName, int dimension) {
        this.client = client;
        this.indexName = indexName;
        this.dimension = dimension;
        
        try {
            createIndexIfNotExists();
        } catch (IOException e) {
            log.error("创建索引失败", e);
            throw new RuntimeException("创建索引失败", e);
        }
    }

    @Override
    public String add(Embedding embedding) {
        return add(embedding, null);
    }

    @Override
    public String add(Embedding embedding, TextSegment textSegment) {
        String id = UUID.randomUUID().toString();
        add(id, embedding);
        return id;
    }

    @Override
    public void add(String id, Embedding embedding) {
        try {
            EmbeddingDocument doc = new EmbeddingDocument();
            doc.setId(id);
            doc.setVector(embedding.vector());
            doc.setText("");
            
            IndexRequest<EmbeddingDocument> request = IndexRequest.of(i -> i
                    .index(indexName)
                    .id(id)
                    .document(doc)
            );
            
            client.index(request);
            log.debug("添加向量到 Elasticsearch: {}", id);
        } catch (IOException e) {
            log.error("添加向量失败", e);
            throw new RuntimeException("添加向量失败", e);
        }
    }

    @Override
    public List<String> addAll(List<Embedding> embeddings) {
        List<String> ids = new ArrayList<>();
        for (Embedding embedding : embeddings) {
            ids.add(add(embedding));
        }
        return ids;
    }

    @Override
    public List<String> addAll(List<Embedding> embeddings, List<TextSegment> textSegments) {
        if (embeddings.size() != textSegments.size()) {
            throw new IllegalArgumentException("embeddings 和 textSegments 数量不匹配");
        }
        
        List<String> ids = new ArrayList<>();
        for (int i = 0; i < embeddings.size(); i++) {
            String id = UUID.randomUUID().toString();
            try {
                EmbeddingDocument doc = new EmbeddingDocument();
                doc.setId(id);
                doc.setVector(embeddings.get(i).vector());
                doc.setText(textSegments.get(i).text());
                
                IndexRequest<EmbeddingDocument> request = IndexRequest.of(ir -> ir
                        .index(indexName)
                        .id(id)
                        .document(doc)
                );
                
                client.index(request);
                ids.add(id);
                log.debug("批量添加向量到 Elasticsearch: {}", id);
            } catch (IOException e) {
                log.error("批量添加向量失败", e);
                throw new RuntimeException("批量添加向量失败", e);
            }
        }
        return ids;
    }

    @Override
    public EmbeddingSearchResult<TextSegment> search(EmbeddingSearchRequest request) {
        List<EmbeddingMatch<TextSegment>> matches = performSearch(
                request.queryEmbedding(),
                request.maxResults(),
                request.minScore()
        );
        return new EmbeddingSearchResult<>(matches);
    }

    /**
     * 执行搜索
     */
    private List<EmbeddingMatch<TextSegment>> performSearch(Embedding referenceEmbedding, int maxResults, double minScore) {
        try {
            // 使用脚本计算余弦相似度
            String scriptSource = """
                double dotProduct = 0.0;
                double normA = 0.0;
                double normB = 0.0;
                for (int i = 0; i < params.queryVector.size(); i++) {
                    dotProduct += params.queryVector.get(i) * doc['vector'][i];
                    normA += params.queryVector.get(i) * params.queryVector.get(i);
                    normB += doc['vector'][i] * doc['vector'][i];
                }
                return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
                """;
            
            // 将 float[] 转换为 List<Float>
            float[] vectorArray = referenceEmbedding.vector();
            List<Float> vectorList = new ArrayList<>(vectorArray.length);
            for (float v : vectorArray) {
                vectorList.add(v);
            }
            
            SearchRequest searchRequest = SearchRequest.of(s -> s
                    .index(indexName)
                    .size(maxResults)
                    .query(q -> q
                            .scriptScore(ss -> ss
                                    .query(Query.of(mq -> mq.matchAll(ma -> ma)))
                                    .script(script -> script
                                            .inline(inline -> inline
                                                    .source(scriptSource)
                                                    .params("queryVector", JsonData.of(vectorList))
                                            )
                                    )
                            )
                    )
            );
            
            SearchResponse<EmbeddingDocument> response = client.search(searchRequest, EmbeddingDocument.class);
            
            return response.hits().hits().stream()
                    .filter(hit -> hit.score() != null && hit.score() >= minScore)
                    .map(hit -> {
                        EmbeddingDocument doc = hit.source();
                        if (doc == null) {
                            return null;
                        }
                        
                        TextSegment textSegment = TextSegment.from(doc.getText());
                        return new EmbeddingMatch<>(
                                hit.score(),
                                hit.id(),
                                Embedding.from(doc.getVector()),
                                textSegment
                        );
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.error("向量检索失败", e);
            throw new RuntimeException("向量检索失败", e);
        }
    }

    /**
     * 创建索引（如果不存在）
     */
    private void createIndexIfNotExists() throws IOException {
        ExistsRequest existsRequest = ExistsRequest.of(e -> e.index(indexName));
        boolean exists = client.indices().exists(existsRequest).value();
        
        if (!exists) {
            CreateIndexRequest createRequest = CreateIndexRequest.of(c -> c
                    .index(indexName)
                    .mappings(m -> m
                            .properties("id", p -> p.keyword(k -> k))
                            .properties("text", p -> p.text(t -> t))
                            .properties("vector", p -> p.denseVector(dv -> dv
                                    .dims(dimension)
                                    .index(true)
                                    .similarity("cosine")
                            ))
                    )
            );
            
            client.indices().create(createRequest);
            log.info("创建 Elasticsearch 索引: {}", indexName);
        }
    }

    /**
     * 嵌入文档
     */
    public static class EmbeddingDocument {
        private String id;
        private float[] vector;
        private String text;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public float[] getVector() {
            return vector;
        }

        public void setVector(float[] vector) {
            this.vector = vector;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
