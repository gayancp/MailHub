package com.example.gayanchinthaka.mailhub.backend.controller;

import com.example.gayanchinthaka.mailhub.backend.dto.CampaignCreateDTO;
import com.example.gayanchinthaka.mailhub.backend.dto.CampaignDetailDTO;
import com.example.gayanchinthaka.mailhub.backend.dto.CampaignSummaryDTO;
import com.example.gayanchinthaka.mailhub.backend.model.CampaignStatus;
import com.example.gayanchinthaka.mailhub.backend.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/campaigns")
@RequiredArgsConstructor
public class CampaignController {

    private final CampaignService campaignService;

    /**
     * GET /api/campaigns — list all campaigns (home page tiles).
     * Optional query param: ?status=RUNNING to filter by status.
     */
    @GetMapping
    public ResponseEntity<List<CampaignSummaryDTO>> getAllCampaigns(
            @RequestParam(required = false) CampaignStatus status) {
        List<CampaignSummaryDTO> campaigns;
        if (status != null) {
            campaigns = campaignService.getCampaignsByStatus(status);
        } else {
            campaigns = campaignService.getAllCampaigns();
        }
        return ResponseEntity.ok(campaigns);
    }

    /**
     * GET /api/campaigns/{id} — get full campaign details.
     */
    @GetMapping("/{id}")
    public ResponseEntity<CampaignDetailDTO> getCampaignById(@PathVariable Long id) {
        return ResponseEntity.ok(campaignService.getCampaignById(id));
    }

    /**
     * POST /api/campaigns — create a new campaign.
     */
    @PostMapping
    public ResponseEntity<CampaignDetailDTO> createCampaign(
            @Valid @RequestBody CampaignCreateDTO dto) {
        CampaignDetailDTO created = campaignService.createCampaign(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * PUT /api/campaigns/{id} — update an existing campaign.
     */
    @PutMapping("/{id}")
    public ResponseEntity<CampaignDetailDTO> updateCampaign(
            @PathVariable Long id,
            @Valid @RequestBody CampaignCreateDTO dto) {
        return ResponseEntity.ok(campaignService.updateCampaign(id, dto));
    }

    /**
     * DELETE /api/campaigns/{id} — delete a campaign.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        campaignService.deleteCampaign(id);
        return ResponseEntity.noContent().build();
    }
}
