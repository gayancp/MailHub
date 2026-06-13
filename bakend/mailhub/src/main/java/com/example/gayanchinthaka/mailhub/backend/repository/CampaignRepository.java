package com.example.gayanchinthaka.mailhub.backend.repository;

import com.example.gayanchinthaka.mailhub.backend.model.Campaign;
import com.example.gayanchinthaka.mailhub.backend.model.CampaignStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CampaignRepository extends JpaRepository<Campaign, Long> {

    List<Campaign> findByStatus(CampaignStatus status);
}
