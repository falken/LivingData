package com.lightstream.demo;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.Insert;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.datastax.driver.core.querybuilder.QueryBuilder.insertInto;
import static com.datastax.driver.core.querybuilder.QueryBuilder.now;

public class PersonService {


    private static final ImmutableMap<String,Pair<String,Function<String,Object>>> mappers = new ImmutableMap.Builder<String, Pair<String,Function<String,Object>>>()
            .put("firstName", Pair.of("first_name",(item)->item))
            .put("lastName", Pair.of("last_name",(item)->item))
            .put("eyeColor", Pair.of("eye_color",(item)->item))
            .put("hairColor", Pair.of("hair_color",(item)->item))
            .put("age", Pair.of("age", Integer::parseInt))
            .put("weight", Pair.of("weight", Integer::parseInt))
            .put("height", Pair.of("height", Integer::parseInt))
            .build();

    private static final ImmutableList<String> properties = ImmutableList.of("person_id", "first_name", "last_name", "eye_color", "hair_color", "age", "height", "weight");
    private final Cluster cluster;

    public PersonService(Cluster cluster) {
        this.cluster = cluster;
    }

    public List<Person> getPeople() {
        List<Person> people = new ArrayList<>();
        try (final Session session = cluster.connect("Demo1")) {
            final ResultSet rs = session.execute("select person_id,person_name from people where organization_id=3ee3da7c-8022-48dc-8eb2-0dff523fc3fa");

            for(Row row:rs){
                people.add(new Person(row.getUUID("person_id"), row.getString("person_name")));
            }

        }
        return people;
    }


    public PersonData getPersonData(UUID personId) {
        PersonData result = new PersonData();
        try (final Session session = cluster.connect("Demo1")) {
            final ResultSet rs = session.execute("select * from person where person_id=" + personId + " ORDER BY update_ts DESC");
            Iterator<Row> iterator = rs.iterator();
            List<String> unsetProperties = new ArrayList<>(properties);
            while (iterator.hasNext() && !unsetProperties.isEmpty()) {
                Row row = iterator.next();

                ColumnDefinitions columnDefinitions = row.getColumnDefinitions();
                columnDefinitions.forEach(def -> {
                    String name = def.getName();
                    DataType type = def.getType();

                    Object value = row.getObject(name);
                    if (value != null && unsetProperties.contains(name)) {
                        result.setValue(name, value);
                        unsetProperties.remove(name);
                    }
                });

            }
        }
        return result;
    }

    public void patchPersonData(UUID personId, Map<String, Object> delta) {
        try (final Session session = cluster.connect("Demo1")) {

            List<Map.Entry<String, Object>> entries = new ArrayList<>(delta.entrySet())
                    .stream().filter(item -> mappers.containsKey(item.getKey())).collect(Collectors.toList());

            Insert query = insertInto("person")
                    .values(entries.stream()
                                    .map(Map.Entry::getKey)
                                    .map((val) -> mappers.get(val).getLeft())
                                    .collect(Collectors.toList()),
                            entries.stream()
                                    .map((val) -> mappers.get(val.getKey()).getRight().apply(val.getValue().toString()))
                                    .collect(Collectors.toList()))
                    .value("person_id", personId)
                    .value("update_ts", now());

            System.out.println(query.getQueryString());
            session.execute(query);
        }
    }


}
