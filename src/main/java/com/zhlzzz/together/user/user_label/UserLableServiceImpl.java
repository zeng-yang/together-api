package com.zhlzzz.together.user.user_label;

import com.zhlzzz.together.user.User;
import com.zhlzzz.together.user.UserNotFoundException;
import com.zhlzzz.together.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class UserLableServiceImpl implements UserLabelService {

    @PersistenceContext
    private EntityManager em;
    private final TransactionTemplate tt;
    private final UserRepository userRepository;
    private final UserLabelRepository userLabelRepository;

    @Override
    public UserLabelEntity addUserLabel(Long userId, String label) throws UserLabelUsedException {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<UserLabelEntity> userLabels= userLabelRepository.findAllByUserId(user.getId());
        Assert.isTrue(userLabels.size() < 10, "个人标签不能超过10个");
        for (UserLabelEntity userLabel : userLabels) {
            if (userLabel.getLabel().equals(label)) {
                throw new UserLabelUsedException(label);
            }
        }

        UserLabelEntity userLabel = new UserLabelEntity();
        userLabel.setLabel(label);
        userLabel.setUserId(user.getId());
        return userLabelRepository.save(userLabel);
    }

    @Override
    public void showUserLabels(Long userId, Set<Long> ids) {
        Assert.isTrue(ids.size() < 6, "最多不超过5个");
        tt.execute((s)-> {
            List<UserLabelEntity> userLabelEntities = em.createQuery("SELECT u FROM UserLabelEntity u WHERE u.userId = :userId", UserLabelEntity.class)
                    .setParameter("userId", userId)
                    .getResultList();
            for (UserLabelEntity userLabelEntity : userLabelEntities) {
                if (ids.contains(userLabelEntity.getId())) {
                    userLabelEntity.setShowed(true);
                } else {
                    userLabelEntity.setShowed(false);
                }
                em.merge(userLabelEntity);
            }
            em.flush();
            return true;
        });
    }

    @Override
    public List<UserLabelEntity> getUserLabelsByUserId(Long userId) {
        return userLabelRepository.findByUserIdAndShowedOrderByIdAsc(userId, true);
    }

    @Override
    public List<UserLabelEntity> getAllByUserId(Long userId) {
        return userLabelRepository.findAllByUserId(userId);
    }

    @Override
    public void delete(Set<Long> ids) {
        tt.execute((s)-> {
            em.createQuery("DELETE FROM UserLabelEntity u WHERE u.id in :ids")
                    .setParameter("ids", ids)
                    .executeUpdate();
            return true;
        });
    }
}
