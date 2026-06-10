package com.Project.SmartHome.Reposatory;

import com.Project.SmartHome.entity.IdentityVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface IdentityVerificationRepository extends JpaRepository<IdentityVerification, Long> {
    List<IdentityVerification> findByUserId(Long userId);
}