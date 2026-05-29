const basePath = import.meta.env.VITE_API_BASE_URL ?? ''

export async function getDefaultApi() {
  const mod: any = await import('./')
  const config = new mod.Configuration({ basePath })
  return new mod.DefaultApi(config)
}

export async function getApi(apiClassName: string) {
  const mod: any = await import('./')
  const config = new mod.Configuration({ basePath })
  const ApiClass = (mod as any)[apiClassName]
  return new ApiClass(config)
}

export const openapiBasePath = basePath
