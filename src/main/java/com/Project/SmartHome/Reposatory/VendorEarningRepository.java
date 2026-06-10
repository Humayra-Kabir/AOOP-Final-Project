package com.Project.SmartHome.Reposatory;

import com.Project.SmartHome.entity.VendorEarning;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VendorEarningRepository extends JpaRepository<VendorEarning, Long> {
    List<VendorEarning> findByVendorId(Long vendorId);
}