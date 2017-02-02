package io.noorulhaq.functional.banking.domain;


import io.noorulhaq.functional.util.FutureReader;
import javaslang.API;
import javaslang.concurrent.Future;
import javaslang.control.Option;
import static javaslang.API.*;
import static javaslang.Patterns.*;

public class App
{
    public static void main( String[] args )
    {



        FutureReader<String,String> future1 = new FutureReader<>( (str) -> Future.of(() -> "Test1"));
        FutureReader<String,String> future2 = new FutureReader<>( (str) -> Future.of(() -> "Test2"));


        Future.of(() -> Option.of("Test1")).flatMap(str -> Match(str).of(Case(Some($()), v -> Future.of( () -> v.concat("")))));


        Future<String> result = future1
                .flatMap( str1 -> future2
                        .flatMap( str2 -> future1
                                .flatMap( str3 -> future2
                                        .flatMap( str4 -> future1
                                        .map(str5 -> str1.concat(str2).concat(str3).concat(str4).concat(str5) ))))).apply("");

        System.out.println(result.get());
    }
}
