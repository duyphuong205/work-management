package com.cloud.work.service.impl;

import com.cloud.work.constants.AppConstants;
import com.cloud.work.constants.MessageConstants;
import com.cloud.work.entity.UserInfo;
import com.cloud.work.exception.NotFoundException;
import com.cloud.work.repository.UserInfoRepository;
import com.cloud.work.service.UserInfoService;
import com.cloud.work.utils.MessageUtils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Throwable.class)
public class UserInfoServiceImpl implements UserInfoService {

    private final EntityManager entityManager;
    private final UserInfoRepository userInfoRepository;

    @Override
    public UserInfo create(UserInfo userInfo) {
        return userInfoRepository.save(userInfo);
    }

    @Override
    public void updateStatusByEmail(String email, String status) {
        userInfoRepository.updateStatusByEmail(email, status);
    }

    @Override
    public UserInfo getByEmail(String email) {
        UserInfo userInfo = userInfoRepository.findByEmail(email);
        if (Objects.isNull(userInfo)) {
            throw new NotFoundException(AppConstants.RES_NOT_FOUND_CODE, MessageUtils.getMessage(MessageConstants.MSG_ACCOUNT_NOT_FOUND));
        }
        return userInfo;
    }

    @Override
    public int countByCondition(Map<String, Object> params) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<UserInfo> root = cq.from(UserInfo.class);

        List<Predicate> ps = new ArrayList<>();
        if (params.get("email") != null) {
            ps.add(cb.equal(root.get("email"), params.get("email")));
        }
        if (params.get("status") != null) {
            ps.add(cb.equal(root.get("status"), params.get("status")));
        }

        cq.select(cb.count(root)).where(ps.toArray(new Predicate[0]));
        Long count = entityManager.createQuery(cq).getSingleResult();
        return Math.toIntExact(count);
    }
}