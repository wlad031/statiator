package dev.vgerasimov.example;

import com.hubspot.jinjava.Jinjava;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyFuncClass {
    public static final Jinjava jinjava1;
    static {
        jinjava1 = new Jinjava();
    }

    public static int i = 5;

    public static Map<String, Object> apply(String s) {
        if ("0".equals(s)) {
            System.out.println("stop");
            return Map.of("a", List.of("trula", 1 ,24));
        }

        Jinjava jinjava = new dev.vgerasimov.example.MyJinjava(jinjava1.getGlobalContext());
        Map<String, Object> context = new HashMap<>();
        context.put("name_1", s);

        String renderedTemplate = jinjava.render("{{ gen('" + i-- +"').a }}hello {{ name_1 }} hello<br>{{ truncate(truncate('hellololasdadad',20), 10) }}", context);

        System.out.println(renderedTemplate);

        return Map.of("a", "hahaha");
    }
}
