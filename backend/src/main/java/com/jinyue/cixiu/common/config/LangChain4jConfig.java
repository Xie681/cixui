package com.jinyue.cixiu.common.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.jinyue.cixiu.common.embedding.ElasticsearchEmbeddingStoreImpl;
import com.jinyue.cixiu.module.chatbot.tools.*;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.dashscope.QwenChatModel;
import dev.langchain4j.model.dashscope.QwenEmbeddingModel;
import dev.langchain4j.model.dashscope.QwenStreamingChatModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStore;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class LangChain4jConfig {

    @Value("${langchain4j.dashscope.chat-model.api-key}")
    private String chatApiKey;

    @Value("${langchain4j.dashscope.embedding-model.api-key}")
    private String embeddingApiKey;

    @Value("${langchain4j.dashscope.chat-model.model-name:qwen-plus}")
    private String chatModelName;

    @Value("${langchain4j.dashscope.chat-model.vision-model-name:qwen-vl-plus}")
    private String visionModelName;

    @Value("${langchain4j.dashscope.embedding-model.model-name:text-embedding-v2}")
    private String embeddingModelName;

    @Value("${langchain4j.dashscope.chat-model.temperature:0.7}")
    private Double temperature;

    @Value("${langchain4j.dashscope.chat-model.max-tokens:2000}")
    private Integer maxTokens;

    @Value("${langchain4j.dashscope.chat-model.top-p:0.8}")
    private Double topP;

    @Value("${elasticsearch.host:localhost}")
    private String esHost;

    @Value("${elasticsearch.port:9200}")
    private Integer esPort;

    @Value("${elasticsearch.username:}")
    private String esUsername;

    @Value("${elasticsearch.password:}")
    private String esPassword;

    @Value("${elasticsearch.index-name:knowledge_embeddings}")
    private String esIndexName;

    private final OrderQueryTool orderQueryTool;
    private final PriceCalculatorTool priceCalculatorTool;
    private final StyleRecommendationTool styleRecommendationTool;
    private final ImageAnalysisTool imageAnalysisTool;

    @Bean
    public ChatLanguageModel chatLanguageModel() {
        return QwenChatModel.builder()
                .apiKey(chatApiKey)
                .modelName(chatModelName)
                .temperature(temperature.floatValue())
                .maxTokens(maxTokens)
                .topP(topP)
                .build();
    }

    @Bean
    public StreamingChatLanguageModel streamingChatLanguageModel() {
        return QwenStreamingChatModel.builder()
                .apiKey(chatApiKey)
                .modelName(chatModelName)
                .temperature(temperature.floatValue())
                .maxTokens(maxTokens)
                .topP(topP)
                .build();
    }

    /**
     * 多模态视觉模型（支持图片理解）
     */
    @Bean
    public ChatLanguageModel visionChatLanguageModel() {
        return QwenChatModel.builder()
                .apiKey(chatApiKey)
                .modelName(visionModelName)
                .temperature(temperature.floatValue())
                .maxTokens(maxTokens)
                .topP(topP)
                .build();
    }

    /**
     * 多模态流式模型
     */
    @Bean
    public StreamingChatLanguageModel visionStreamingChatLanguageModel() {
        return QwenStreamingChatModel.builder()
                .apiKey(chatApiKey)
                .modelName(visionModelName)
                .temperature(temperature.floatValue())
                .maxTokens(maxTokens)
                .topP(topP)
                .build();
    }

    /**
     * 千问嵌入模型（向量化文本）
     * 支持中文，1536 维向量
     */
    @Bean
    public EmbeddingModel embeddingModel() {
        return QwenEmbeddingModel.builder()
                .apiKey(embeddingApiKey)
                .modelName(embeddingModelName)
                .build();
    }

    /**
     * Elasticsearch 客户端
     */
    @Bean
    public ElasticsearchClient elasticsearchClient() {
        RestClient restClient;
        
        if (esUsername != null && !esUsername.isEmpty()) {
            // 使用认证
            CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
            credentialsProvider.setCredentials(
                    AuthScope.ANY,
                    new UsernamePasswordCredentials(esUsername, esPassword)
            );
            
            restClient = RestClient.builder(new HttpHost(esHost, esPort, "http"))
                    .setHttpClientConfigCallback(httpClientBuilder ->
                            httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider))
                    .build();
        } else {
            // 不使用认证
            restClient = RestClient.builder(new HttpHost(esHost, esPort, "http")).build();
        }
        
        RestClientTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
        
        return new ElasticsearchClient(transport);
    }

    /**
     * Elasticsearch 向量存储
     */
    @Bean
    public EmbeddingStore<TextSegment> embeddingStore(ElasticsearchClient elasticsearchClient) {
        return new ElasticsearchEmbeddingStoreImpl(
                elasticsearchClient,
                esIndexName,
                1536  // 千问 text-embedding-v2 模型的向量维度
        );
    }
}
