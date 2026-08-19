package com.servicehub.crm.vendor.service;

import com.servicehub.common.exceptions.ResourceNotFoundException;
import com.servicehub.crm.mapper.VendorMapper;
import com.servicehub.crm.vendor.dto.VendorResponse;
import com.servicehub.crm.vendor.dto.VendorSaveRequest;
import com.servicehub.crm.vendor.entity.Vendor;
import com.servicehub.crm.vendor.repository.VendorRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

public class VendorServiceImpl implements VendorService {

    private final VendorRepository vendorRepository;
    private final VendorMapper vendorMapper;
    private final SessionFactory sessionFactory;   // injected directly, alongside the repository — no separate layer

    public VendorServiceImpl(VendorRepository vendorRepository, VendorMapper vendorMapper, SessionFactory sessionFactory) {
        this.vendorRepository = vendorRepository;
        this.vendorMapper = vendorMapper;
        this.sessionFactory = sessionFactory;
    }

    // ---------- FETCH ----------
    @Override
    @Transactional(readOnly = true)
    public VendorResponse getById(Long id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "CRM-404-001", "Vendor not found: " + id));
        return vendorMapper.toResponse(vendor);
    }

    // ---------- SAVE (create or update) ----------
    @Override
    @Transactional
    @CacheEvict(cacheNames = "vendorSummaries", key = "#request.id()", condition = "#request.id() != null")
    public VendorResponse save(VendorSaveRequest request) {
        Vendor vendor;
        if (request.id() != null) {
            vendor = vendorRepository.findById(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(HttpStatus.NOT_FOUND, "CRM-404-001", "Vendor not found: " + request.id()));
//            vendorMapper.updateEntityFromRequest(request, vendor);
        } else {
            vendor = vendorMapper.toEntity(request);
        }
        Vendor saved = vendorRepository.save(vendor);
        return vendorMapper.toResponse(saved);
    }

    // ---------- Manual query via SessionFactory — the case JpaRepository doesn't cover well ----------
    @Override
    @Transactional(readOnly = true)
    public List<VendorResponse> findTopRated(BigDecimal minRating) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT v FROM Vendor v WHERE v.avgRating >= :minRating ORDER BY v.avgRating DESC";
        List<Vendor> vendors = session.createQuery(hql, Vendor.class)
                .setParameter("minRating", minRating)
                .list();
        return vendors.stream().map(vendorMapper::toResponse).toList();
    }
}
