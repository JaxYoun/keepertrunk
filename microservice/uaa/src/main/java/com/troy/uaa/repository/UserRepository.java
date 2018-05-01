package com.troy.uaa.repository;

import com.troy.uaa.domain.User;

import java.time.ZonedDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends BaseRepository<User,Long> {

//    Optional<User> findOneByActivationKey(String activationKey);
//
//    List<User> findAllByActivatedIsFalseAndCreatedDateBefore(ZonedDateTime dateTime);
//
//    Optional<User> findOneByResetKey(String resetKey);
//
//    Optional<User> findOneByEmail(String email);
//
    Optional<User> findOneByLogin(String login);
//
    @EntityGraph(attributePaths = "authorities")
    User findOneWithAuthoritiesById(Long id);
//
    @EntityGraph(attributePaths = "userRoleList")
    Optional<User> findOneWithAuthoritiesByLogin(String login);
//
//    Page<User> findAllByLoginNot(Pageable pageable, String login);
    User findUserByLogin(String login);

    @Modifying
    @Query("DELETE FROM User u WHERE u.id IN (?1) ")
    public void deleteByIds(List<Long> ids);

    @Query("SELECT u.password FROM User u WHERE u.id = ?1")
    public String findPassWord(Long id);

    @Modifying
    @Query("UPDATE User u SET u.password = ?1 WHERE u.id = ?2")
    public void updatePassword(String password,Long userId);

    @Modifying
    @Query("DELETE FROM User u WHERE u.tenantId = ?1 ")
    public void deleteByTenantId(Long tenantId);

    @Query("select u.tenantId from User u where u.id = ?1")
    public Long findTenantId(Long id);

    @Query("select u from User u where u.login = ?1 and u.password =?2 ")
    public User findByCondition(String login,String password);

    @Query("select u.id from User u where u.tenantId = ?1")
    public List<Long> findIdByTenantId(Long id);
}
