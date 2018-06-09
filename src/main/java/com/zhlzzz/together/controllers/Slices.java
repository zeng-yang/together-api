package com.zhlzzz.together.controllers;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhlzzz.together.data.Direction;
import com.zhlzzz.together.data.Slice;
import com.zhlzzz.together.data.SliceIndicator;
import com.zhlzzz.together.data.SliceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.AlternateTypeRules;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.schema.WildcardType;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResolvedMethodParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.schema.ModelBuilderPlugin;
import springfox.documentation.spi.schema.contexts.ModelContext;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class Slices {
    @Slf4j
    @RequiredArgsConstructor
    public static class SliceIndicatorsMethodArgumentResolver implements HandlerMethodArgumentResolver {

        private final ApplicationContext context;
//        private final ConversionService conversionService;

        public ConversionService getConversionService() {
            return context.getBean(ConversionService.class);
        }

        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return SliceIndicator.class.isAssignableFrom(parameter.getParameterType());
        }

        @Override
        @SuppressWarnings("unchecked")
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            ConversionService conversionService = getConversionService();

            Class cursorType = (Class) ((ParameterizedType) (parameter.getGenericParameterType())).getActualTypeArguments()[0];
            if (!conversionService.canConvert(String.class, cursorType)) {
                return new SliceRequest<>(null, 50);
            }

            String cursorStr = webRequest.getParameter("cursor");
            String limitStr = webRequest.getParameter("limit");
            String directionStr = webRequest.getParameter("direction");

            Object cursor = conversionService.convert(cursorStr, cursorType);
            Integer limit = conversionService.convert(limitStr, Integer.class);
            Direction direction = directionStr != null && directionStr.equals("backward") ? Direction.BACKWARD : Direction.FORWARD;

            return new SliceRequest<Object>(cursor, limit == null || limit < 1 ? com.zhlzzz.together.data.Slices.DEFAULT_PAGE_SIZE : limit, direction);
        }
    }

    @Slf4j
    @Profile({"dev", "test"})
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 2000)
    @Component
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class SliceIndicatorSpringfoxOperationPlugin implements OperationBuilderPlugin {

        private final TypeResolver typeResolver;

        @Override
        public void apply(OperationContext context) {
            List<Parameter> parameters = new ArrayList<>(2);
            for (ResolvedMethodParameter parameter : context.getParameters()) {
                if (!parameter.getParameterType().isInstanceOf(SliceIndicator.class)) {
                    continue;
                }
                parameters.add(buildParameter("cursor", String.class, "列表游标，默认为列表头", null));
                parameters.add(buildParameter("limit", Integer.class, "列表大小，默认为" + com.zhlzzz.together.data.Slices.DEFAULT_PAGE_SIZE, null));
                parameters.add(buildParameter("direction", String.class, "遍历方向，forward为正向，backward为反向，默认为 forward", null));
            }
            if (!parameters.isEmpty()) {
                context.operationBuilder().parameters(parameters);
            }
        }

        private Parameter buildParameter(String name, Type type, String description, String defaultValue) {
            ParameterBuilder builder = new ParameterBuilder();
            builder.name(name);
            builder.modelRef(new ModelRef("string"));
            builder.type(typeResolver.resolve(type));
            builder.required(false);
            builder.description(description);
            builder.defaultValue(defaultValue);
            builder.parameterType("query");
            return builder.build();
        }

        @Override
        public boolean supports(DocumentationType delimiter) {
            return true;
        }


    }

    @Slf4j
    @Profile({"dev", "test"})
    @Order(SwaggerPluginSupport.SWAGGER_PLUGIN_ORDER + 1000)
    @Component
    @RequiredArgsConstructor(onConstructor = @__(@Autowired))
    public static class SliceSpringfoxModelPlugin implements ModelBuilderPlugin {

        private final TypeResolver typeResolver;

        @Override
        public void apply(ModelContext context) {
            ResolvedType resolvedType = typeResolver.resolve(context.getType());
            if (!resolvedType.isInstanceOf(Slice.class)) {
                return;
            }
            List<ResolvedType> typeParameters = resolvedType.getTypeParameters();
            if (typeParameters.isEmpty()) {
                return;
            }
            ResolvedType itemType = typeParameters.get(0);
            context.getAlternateTypeProvider().addRule(AlternateTypeRules.newRule(
                    typeResolver.resolve(Slice.class, itemType, WildcardType.class),
                    typeResolver.resolve(List.class, itemType)
            ));
        }

        @Override
        public boolean supports(DocumentationType delimiter) {
            return DocumentationType.SWAGGER_2.equals(delimiter);
        }
    }

    @Slf4j
    public static class SliceHttpMessageConverter extends AbstractHttpMessageConverter<Slice> {

        private final ObjectMapper objectMapper;

        public SliceHttpMessageConverter(ObjectMapper objectMapper) {
            super(Charset.forName("UTF-8"), MediaType.APPLICATION_JSON_UTF8);
            this.objectMapper = objectMapper;
        }

        @Override
        protected boolean supports(Class<?> clazz) {
            return Slice.class.isAssignableFrom(clazz);
        }

        @Override
        protected Slice readInternal(Class<? extends Slice> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            return null;
        }

        @Override
        protected void writeInternal(Slice slice, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
            if (slice.getNextIndicator() != null && slice.getNextIndicator().getCursor() != null) {
                outputMessage.getHeaders().add("X-Slice-Next-Cursor", slice.getNextIndicator().getCursor().toString());
            }

            if (slice.getTotalCount() != null) {
                outputMessage.getHeaders().add("X-Slice-Total-Count", slice.getTotalCount().toString());
            }

            objectMapper.writeValue(outputMessage.getBody(), slice.getItems());
        }
    }
}
