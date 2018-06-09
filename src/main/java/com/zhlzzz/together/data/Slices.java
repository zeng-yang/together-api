package com.zhlzzz.together.data;

import com.zhlzzz.together.utils.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Nonnull;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public final class Slices {

    public static final int DEFAULT_PAGE_SIZE = 200;

    private static <T, I> Slice<T, I> emptySlice(SliceIndicator<I> indicator, Integer totalCount) {
        return new SliceImpl<>(indicator, Collections.emptyList(), null, totalCount);
    }

    private static Sort reverseSort(Sort sort) {
        if (sort.isUnsorted()) {
            return sort;
        }
        List<Sort.Order> orders = new ArrayList<>();
        for (Sort.Order order : sort) {
            Sort.Direction direction = order.isAscending() ?
                    Sort.Direction.DESC : Sort.Direction.ASC;
            Sort.NullHandling nullHandling;
            if (order.getNullHandling() == Sort.NullHandling.NULLS_FIRST) {
                nullHandling = Sort.NullHandling.NULLS_LAST;
            } else if (order.getNullHandling() == Sort.NullHandling.NULLS_LAST) {
                nullHandling = Sort.NullHandling.NULLS_FIRST;
            } else {
                nullHandling = Sort.NullHandling.NATIVE;
            }
            orders.add(new Sort.Order(direction, order.getProperty(), nullHandling));
        }
        return Sort.by(orders);
    }

    public static <T> Slice<T, Integer> ofSpringPage(
            @Nonnull Function<Pageable, Page<T>> pageFetcher,
            @Nonnull SliceIndicator<Integer> indicator,
            Sort forwardSort
    ) {

        int page = indicator.getCursor() == null || indicator.getCursor() <= 0 ? 1 : indicator.getCursor();
        Sort sort = indicator.getDirection().isForward() ? forwardSort : reverseSort(forwardSort);
        PageRequest pageRequest = PageRequest.of(page, indicator.getLimit() + 1, sort);
        Page<T> springPage = pageFetcher.apply(pageRequest);
        Integer totalCount = Long.valueOf(springPage.getTotalElements()).intValue();
        if (!springPage.hasContent()) {
            return emptySlice(indicator, totalCount);
        }

        Integer nextCursor = springPage.getNumberOfElements() < (indicator.getLimit() + 1) ?
                null : page + 1;
        List<T> content = springPage.getContent().subList(0, Math.min(springPage.getNumberOfElements(), indicator.getLimit()));

        return new SliceImpl<>(indicator, content, nextCursor, totalCount);
    }

    public static <T> Slice<T, Integer> of(
            @Nonnull Function<Pageable, List<T>> contentFetcher,
            @Nonnull SliceIndicator<Integer> indicator
    ) {
        return of(contentFetcher, indicator, Sort.unsorted(), null);
    }

    public static <T> Slice<T, Integer> of(
            @Nonnull Function<Pageable, List<T>> contentFetcher,
            @Nonnull SliceIndicator<Integer> indicator,
            @Nonnull Supplier<Integer> totalCountSupplier
    ) {
        return of(contentFetcher, indicator, Sort.unsorted(), totalCountSupplier);
    }

    public static <T> Slice<T, Integer> of(
            @Nonnull Function<Pageable, List<T>> contentFetcher,
            @Nonnull SliceIndicator<Integer> indicator,
            Sort forwardSort,
            Supplier<Integer> totalCountSupplier
    ) {
        Integer totalCount = null;
        if (totalCountSupplier != null) {
            totalCount = totalCountSupplier.get();
        }

        if (indicator.getLimit() <= 0) {
            return emptySlice(indicator, totalCount);
        }

        int page = indicator.getCursor() == null || indicator.getCursor() <= 0 ? 1 : indicator.getCursor();
        Sort sort = indicator.getDirection().isForward() ? forwardSort : reverseSort(forwardSort);
        PageRequest pageRequest = PageRequest.of(page, indicator.getLimit() + 1, sort);
        List<T> contentList = contentFetcher.apply(pageRequest);
        if (contentList.isEmpty()) {
            return emptySlice(indicator, totalCount);
        }

        Integer nextCursor = contentList.size() < (indicator.getLimit() + 1) ?
                null : page + 1;
        List<T> content = contentList.subList(0, Math.min(contentList.size(), indicator.getLimit()));

        return new SliceImpl<>(indicator, content, nextCursor, totalCount);
    }

    public static <T> Slice<T, Integer> of(
            @Nonnull EntityManager em,
            @Nonnull CriteriaQuery<T> criteriaQuery,
            @Nonnull SliceIndicator<Integer> indicator,
            CriteriaQuery<Long> countCriteriaQuery
    ) {
        Integer totalCount = null;
        if (countCriteriaQuery != null) {
            TypedQuery<Long> countQuery = em.createQuery(countCriteriaQuery);
            totalCount = countQuery.getSingleResult().intValue();
        }
        if (indicator.getLimit() <= 0) {
            return emptySlice(indicator, totalCount);
        }

        if (indicator.getDirection().isBackward() &&
                !criteriaQuery.getOrderList().isEmpty()) {
            List<Order> reverseOrders = CollectionUtils.map(criteriaQuery.getOrderList(), (order) -> order.reverse());
            criteriaQuery.orderBy(reverseOrders);
        }

        int page = indicator.getCursor() == null || indicator.getCursor() <= 0 ? 1 : indicator.getCursor();
        int offset = (page - 1) * indicator.getLimit();
        TypedQuery<T> query = em.createQuery(criteriaQuery);
        query.setMaxResults(indicator.getLimit() + 1);
        query.setFirstResult(offset);
        List<T> contentList =  query.getResultList();

        Integer nextCursor = contentList.size() < (indicator.getLimit() + 1) ?
                null : page + 1;
        List<T> content = contentList.subList(0, Math.min(contentList.size(), indicator.getLimit()));

        return new SliceImpl<>(indicator, content, nextCursor, totalCount);
    }

    public static void setupQueryWithPage(Query query, SliceIndicator<Integer> indicator) {
        int page = indicator.getCursor() == null || indicator.getCursor() <= 0 ? 1 : indicator.getCursor();
        query.setMaxResults(indicator.getLimit())
                .setFirstResult((page - 1) * indicator.getLimit());
    }
}
