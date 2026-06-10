package com.Project.SmartHome.Reposatory;

import com.Project.SmartHome.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<Property, Long> {
    List<Property> findByVendorId(Long vendorId);
}