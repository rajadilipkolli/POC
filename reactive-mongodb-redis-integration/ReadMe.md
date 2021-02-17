
Currently, `@Cacheable` and other annotated forms do not integrate the cache well. Both `Mono/Flux` objects do not implement Serializable and cannot pass the default serializer.