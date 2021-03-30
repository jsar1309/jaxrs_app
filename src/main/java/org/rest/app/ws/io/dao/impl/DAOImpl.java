package org.rest.app.ws.io.dao.impl;

import org.hibernate.query.Query;
import org.rest.app.ws.io.dao.AbstractSessionManager;
import org.rest.app.ws.io.dao.DAO;
import org.rest.app.ws.io.entity.UserEntity;
import org.rest.app.ws.shared.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
@Transactional
public class DAOImpl extends AbstractSessionManager implements DAO {

    @Override
    public UserDTO getUserByUserName(String userName) {

        UserDTO userDTO = null;

        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();

        CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);

        Root<UserEntity> profileRoot = criteriaQuery.from(UserEntity.class);
        criteriaQuery.select(profileRoot);
        criteriaQuery.where(criteriaBuilder.equal(profileRoot.get("email"), userName));

        Query<UserEntity> query = getSession().createQuery(criteriaQuery);
        List<UserEntity> list = query.getResultList();

        if(list != null && list.size() > 0){
            UserEntity userEntity = list.get(0);
            userDTO = new UserDTO();
            BeanUtils.copyProperties(userEntity, userDTO);
        }

        return userDTO;
    }

    @Override
    public UserDTO registerInDB(UserDTO userDTO) throws Exception {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        persist(userEntity);
        return userDTO;
    }

    @Override
    public UserDTO getUserFromDB(Integer id) throws Exception {

        UserDTO userDTO = new UserDTO();
        CriteriaBuilder criteriaBuilder = getSession().getCriteriaBuilder();
        CriteriaQuery<UserEntity> criteriaQuery = criteriaBuilder.createQuery(UserEntity.class);
        Root<UserEntity> profileRoot = criteriaQuery.from(UserEntity.class);
        criteriaQuery.select(profileRoot);
        criteriaQuery.where(criteriaBuilder.equal(profileRoot.get("id"), id));

        Query<UserEntity> query = getSession().createQuery(criteriaQuery);
        List<UserEntity> list = query.getResultList();

        if(list != null && list.size() > 0){
            UserEntity userEntity = list.get(0);
            BeanUtils.copyProperties(userEntity, userDTO);
        }

        return userDTO;
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();
        BeanUtils.copyProperties(userDTO, userEntity);
        update(userEntity);
        return userDTO;
    }


}
