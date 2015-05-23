/*
 * Copyright (C) 2009-2015 Typesafe Inc. <http://www.typesafe.com>
 */
package play.it.http;

import play.libs.F;
import play.mvc.*;
import play.test.Helpers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class ActionCompositionOrderTest {

    @With(ControllerComposition.class)
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ControllerAnnotation {}

    static class ControllerComposition extends Action<ControllerAnnotation> {
        @Override
        public F.Promise<Result> call(Http.Context ctx) throws Throwable {
            return delegate.call(ctx).map(result -> {
                String newContent = "controller" + Helpers.contentAsString(result);
                return Results.ok(newContent);
            });
        }
    }

    @With(ActionComposition.class)
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface ActionAnnotation {}

    static class ActionComposition extends Action<ControllerAnnotation> {
        @Override
        public F.Promise<Result> call(Http.Context ctx) throws Throwable {
            return delegate.call(ctx).map(result -> {
                String newContent = "action" + Helpers.contentAsString(result);
                return Results.ok(newContent);
            });
        }
    }
}