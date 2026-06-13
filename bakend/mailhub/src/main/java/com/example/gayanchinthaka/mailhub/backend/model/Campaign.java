package com.example.gayanchinthaka.mailhub.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private CampaignStatus status = CampaignStatus.DRAFT;

    @Column(nullable = false)
    @Builder.Default
    private int totalEmails = 0;

    @Column(nullable = false)
    @Builder.Default
    private int sentEmails = 0;

    @Column(nullable = false)
    @Builder.Default
    private int deliveredEmails = 0;

    @Column(nullable = false)
    @Builder.Default
    private int clickedEmails = 0;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String templateContent;

    @Column(nullable = false)
    @Builder.Default
    private boolean isHtml = false;

    @Column(nullable = false)
    @Builder.Default
    private long sendIntervalMs = 1000L;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    /**
     * Derived field: completion percentage based on sent vs total emails.
     */
    @Transient
    public double getCompletionPercentage() {
        if (totalEmails == 0) return 0.0;
        return ((double) sentEmails / totalEmails) * 100.0;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
