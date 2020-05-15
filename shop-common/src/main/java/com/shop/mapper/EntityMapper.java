package com.shop.mapper;

import com.shop.utils.EntityUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * mapper class to transfer entity and its dto to each other.
 *
 * @param <E> Entity Class
 * @param <D> DTO Class
 */
public abstract class EntityMapper<E, D> {

    protected final Class<E> entityClazz;

    protected final Class<D> dtoClazz;

    public final static String[] STANDARD_IGNORED_PROPERTIES =
            new String[]{"organizationId", "createdBy", "createdDate", "lastModifiedBy", "lastModifiedDate"};

    @SuppressWarnings("unchecked")
    public EntityMapper() {
        Class<?>[] genericTypes = GenericTypeResolver.resolveTypeArguments(getClass(), EntityMapper.class);
        this.entityClazz = (Class<E>) genericTypes[0];
        this.dtoClazz = (Class<D>) genericTypes[1];
    }

    public D entityToDto(E entity, String... ignoreProperties) {
        if (entity == null) {
            return null;
        }
        D dto = BeanUtils.instantiate(this.dtoClazz);
        copyPropertiesFromEntity(entity, dto, ignoreProperties);
        return dto;
    }

    public E dtoToEntity(D dto, String... ignoreProperties) {
        if (dto == null) {
            return null;
        }
        return dtoToEntity(dto, null, false, ArrayUtils.addAll(ignoreProperties, STANDARD_IGNORED_PROPERTIES));
    }

    // override if necessary
    protected void copyPropertiesFromEntity(E source, D target, String... ignoreProperties) {
        BeanUtils.copyProperties(source, target, ignoreProperties);
    }

    /**
     * copy properties from source DTO to target eneity, if target entity is null, a new entity instance will be created.
     * ignoreNullValue parameter does not work for Collection(exclude Map) type inner object
     *
     * @param source           DTO instance to be copied from
     * @param target           entity instance to be copied to
     * @param ignoreNullValue  if null value in source DTO should be ignored
     * @param ignoreProperties properties in source DTO are ignored
     * @return target entity with copied value if target is not null, otherwise will return a new instance with the copied
     * value
     */
    public E dtoToEntity(D source, E target, boolean ignoreNullValue, String... ignoreProperties) {
        if (source != null) {
            if (target == null) {
                target = BeanUtils.instantiate(this.entityClazz);
            }
            copyPropertiesFromDto(source, target, ignoreNullValue, ignoreProperties);
        } else if (!ignoreNullValue) {
            target = null;
        }
        return target;
    }

    /**
     * copy properties from source to target, overide this method if with any complex properties.
     *
     * @param source           DTO instance to be copied from, must not be null
     * @param target           entity instance to be copied to, must not be null
     * @param ignoreNullValue  if null value in source DTO should be ignored
     * @param ignoreProperties properties in source DTO are ignored
     */
    public void copyPropertiesFromDto(D source, E target, boolean ignoreNullValue, String... ignoreProperties) {
        EntityUtil.copyProperties(source, target, ignoreNullValue, ignoreProperties);
    }

    /**
     * copy properties from source DTO {@link Map} to the target Entity {@link Map}.
     *
     * @param sources          {@link Map} with DTO instances
     * @param targets          {@link Map} with Entity instance
     * @param ignoreNullValue  if null value in source DTO should be ignored
     * @param ignoreProperties properties in source DTO are ignored
     * @return targets {@link Map} with copied value if target is not empty, otherwise will return a new instance with the
     * copied value from source @see {@link #dtosToEntities(Map, String...)}
     */
    @SuppressWarnings({"unchecked"})
    public <K, V, R> Map<K, R> copyPropertiesFromDtos(Map<K, V> sources, Map<K, R> targets, boolean ignoreNullValue,
                                                      String... ignoreProperties) {
        if (MapUtils.isNotEmpty(sources)) {
            if (MapUtils.isEmpty(targets)) {
                return dtosToEntities(sources, ignoreProperties);
            }
            if (!ignoreNullValue) {
                // remove the entry which is not in source
                targets.keySet().retainAll(sources.keySet());
                return sources.entrySet()//
                        .stream()//
                        .filter(entry -> {
                            if (entry.getValue() == null) {
                                targets.remove(entry.getKey());
                                return false;
                            }
                            return true;
                        }).collect(//
                                Collectors.toMap(//
                                        Entry::getKey, //
                                        entry -> (R) copyPropertiesInternal(//
                                                entry.getValue(), //
                                                targets.get(entry.getKey()), //
                                                ignoreNullValue, //
                                                ignoreProperties//
                                        ))//
                        );
            }
            // ignore null value and both sources and targets have values
            sources.entrySet()//
                    .stream()//
                    .filter(entry -> entry.getValue() != null)//
                    .forEach(entry -> {
                        targets.put(//
                                entry.getKey(), //
                                (R) copyPropertiesInternal(//
                                        entry.getValue(), //
                                        targets.get(entry.getKey()), //
                                        ignoreNullValue, //
                                        ignoreProperties//
                                ));
                    });
        } else if (!ignoreNullValue) {
            return null;
        }
        return targets;
    }

    /**
     * copy properties from source DTO {@link Map} to new Entity {@link Map}.
     *
     * @param sources          {@link Map} with DTO instances
     * @param ignoreProperties properties in source DTO are ignored
     * @return new Entity {@link Map} with the copied value from source
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private <K, V, R> Map<K, R> dtosToEntities(Map<K, V> sources, String... ignoreProperties) {
        if (sources == null) {
            return new LinkedHashMap<>();
        }
        return sources.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Entry::getKey,
                                entry -> {
                                    if (entry.getValue() instanceof Map) {
                                        return (R) dtosToEntities((Map) entry.getValue(), ignoreProperties);
                                    } else if (entry.getValue() instanceof List) {
                                        return (R) dtosToEntities((List) entry.getValue(), ignoreProperties);
                                    }
                                    return (R) dtoToEntity((D) entry.getValue(), ignoreProperties);
                                }//
                        )//
                );
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Object copyPropertiesInternal(Object source, Object target, boolean ignoreNullValue,
                                          String... ignoreProperties) {
        if (source instanceof Map) {
            return copyPropertiesFromDtos((Map) source, (Map) target, ignoreNullValue, ignoreProperties);
        } else if (source instanceof List) {
            return dtosToEntities((List) source, ignoreProperties);
        }
        return dtoToEntity((D) source, (E) target, ignoreNullValue, ignoreProperties);
    }

    public List<D> entitiesToDtos(List<E> entities, String... ignoreProperties) {
        if (entities == null) {
            return new ArrayList<>();
        }
        return entities.stream().map(entity -> this.entityToDto(entity, ignoreProperties)).collect(Collectors.toList());
    }

    public Page<D> entitiesToDtos(Page<E> entities, String... ignoreProperties) {
        if (entities == null) {
            return new PageImpl<>(Collections.emptyList());
        }
        return entities.map(entity -> this.entityToDto(entity, ignoreProperties));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public <K, V, R> Map<K, R> entitiesToDtos(Map<K, V> entities, String... ignoreProperties) {
        if (entities == null) {
            return new LinkedHashMap<>();
        }
        return entities.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                Entry::getKey,
                                entry -> {
                                    if (entry.getValue() instanceof Map) {
                                        return (R) entitiesToDtos((Map) entry.getValue(), ignoreProperties);
                                    } else if (entry.getValue() instanceof List) {
                                        return (R) entitiesToDtos((List) entry.getValue(), ignoreProperties);
                                    }
                                    return (R) entityToDto((E) entry.getValue(), ignoreProperties);
                                }
                        )
                );
    }

    public List<E> dtosToEntities(List<D> dtos, String... ignoreProperties) {
        if (dtos == null) {
            return new ArrayList<>();
        }
        return dtos.stream().map(entry -> this.dtoToEntity(entry, ignoreProperties)).collect(Collectors.toList());
    }

    /**
     * verify if property is ignored
     * <p>
     * if the propName is excatly match with a item in ignoreProperties, then all other properties start with "propName."
     * will also be removed, and then return true.
     *
     * <p>
     * if the "propName." match with any item in ignoreProperties, then the "propName." will be removed.
     *
     * @param propName         property to be verified
     * @param ignoreProperties ignore properties
     * @return if property is ignored
     */
    protected boolean isPropIgnored(String propName, String[] ignoreProperties) {
        if (ignoreProperties == null || ignoreProperties.length == 0) {
            return false;
        }
        boolean excatlyMatch = false;
        String prefix = propName + ".";
        Map<Integer, String> removeableProps = new HashMap<>();
        for (int i = 0; i < ignoreProperties.length; i++) {
            String ignoreProp = ignoreProperties[i];
            if (ignoreProp == null) {
                continue;
            }
            if (ignoreProp.equals(propName)) {
                excatlyMatch = true;
                removeableProps.put(i, ignoreProp);
            } else if (ignoreProp.startsWith(prefix)) {
                removeableProps.put(i, ignoreProp);
            }
        }
        if (excatlyMatch) {
            removeableProps.keySet().forEach(key -> {
                ignoreProperties[key] = null;
            });
        } else {
            removeableProps.entrySet().forEach(entry -> {
                ignoreProperties[entry.getKey()] = entry.getValue().substring(0, propName.length() + 2);
            });
        }
        return excatlyMatch;
    }

    // this method just leave here for future reference, it is an idea on handling delta update for list, it should not be
    // used now
    @SuppressWarnings("unused")
    private List<E> copyPropertiesFromDtos(List<D> sources, List<E> targets, boolean ignoreNullValue,
                                           String... ignoreProperties) {
        if (CollectionUtils.isNotEmpty(sources)) {
            if (CollectionUtils.isEmpty(targets) || !ignoreNullValue) {
                return dtosToEntities(sources, ignoreProperties);
            }
            int loopSize = Math.min(sources.size(), targets.size());
            IntStream.range(0, loopSize).forEach(i -> {
                D source = sources.get(i);
                if (source == null) {
                    // filter the null value in sources as ignore null value must be true here
                    return;
                }
                targets.set(i,
                        dtoToEntity(//
                                source, //
                                targets.get(i), //
                                true, // must be true here
                                ignoreProperties)//
                );
            });
            if (sources.size() > targets.size()) {
                targets.addAll(dtosToEntities(sources.subList(targets.size(), sources.size())));
            }
        } else if (!ignoreNullValue) {
            return null;
        }
        return targets;
    }

    @SuppressWarnings("unused") // use copyPropertiesFromDtos instead
    private <T> Map<T, E> copyPropertiesFromMap(Map<T, D> sources, Map<T, E> targets, boolean ignoreNullValue,
                                                String... ignoreProperties) {
        if (MapUtils.isNotEmpty(sources)) {
            if (MapUtils.isEmpty(targets) || !ignoreNullValue) {
                return dtosToEntities(sources, ignoreProperties);
            }
            // ignore null value and both sources and targets have values
            sources.entrySet()//
                    .stream()//
                    .filter(entry -> entry.getValue() != null)//
                    .forEach(entry -> {
                        targets.put(entry.getKey(), //
                                dtoToEntity(//
                                        entry.getValue(), //
                                        targets.get(entry.getKey()), //
                                        true, // must be true here
                                        ignoreProperties)//
                        );
                    });
        } else if (!ignoreNullValue) {
            return null;
        }
        return targets;
    }

}
