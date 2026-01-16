package com.cloud.work.service.impl;

import com.cloud.work.constants.FieldConstants;
import com.cloud.work.entity.TokenHistory;
import com.cloud.work.enums.Status;
import com.cloud.work.repository.TokenHistoryRepository;
import com.cloud.work.service.TokenHistoryService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class TokenHistoryServiceImpl implements TokenHistoryService {

    private final EntityManager entityManager;
    private final TokenHistoryRepository tokenHistoryRepository;

    @Override
    public void insert(TokenHistory tokenHistory) {
        tokenHistoryRepository.save(tokenHistory);
    }

    @Override
    public void expireToken(Map<String, Object> params) {
        try {
            CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
            CriteriaUpdate<TokenHistory> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(TokenHistory.class);
            Root<TokenHistory> root = criteriaUpdate.from(TokenHistory.class);

            criteriaUpdate.set(FieldConstants.STATUS, Status.EXPD.name());

            Predicate[] arrPredicate = addQueryCondition(criteriaBuilder, root, params);
            criteriaUpdate.where(arrPredicate);
            entityManager.createQuery(criteriaUpdate).executeUpdate();
        } catch (Exception e) {
            log.error(">>> Error BKTokenServiceImpl expireToken()", e);
        }
    }

    @Override
    public boolean isValidToken(String token) {
        return tokenHistoryRepository.countByToken(token) > 0;
    }

    private Predicate[] addQueryCondition(CriteriaBuilder criteriaBuilder, Root<?> root, Map<String, Object> params) {
        params.values().removeAll(Collections.singleton(null));
        params.values().removeAll(Collections.singleton(""));
        List<Predicate> predicates = new ArrayList<>();
        for (String key : params.keySet()) {
            switch (key) {
                case FieldConstants.TOKEN ->
                        predicates.add(criteriaBuilder.equal(root.get(FieldConstants.TOKEN), params.get(key)));
                case FieldConstants.USER_ID ->
                        predicates.add(criteriaBuilder.equal(root.get(FieldConstants.USER_ID), Long.parseLong((String) params.get(key))));
            }
        }
        Predicate[] predicatesArr = new Predicate[predicates.size()];
        return predicates.toArray(predicatesArr);
    }
}
