package com.joelbgreenberg.humaninterest;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.time.Instant;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true )
public class Change {

    private String id;
    private String jobId;
    private String type;
    private Date date;
    private String status;
    private Job data;


    public Date getAnnounceDate() {
        return announceDate;
    }

    public void setAnnounceDate(Date announceDate) {
        this.announceDate = announceDate;
    }

    private Date  announceDate;

    public Instant getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Instant createAt) {
        this.createAt = createAt;
    }

    private Instant createAt;
    private Instant createId;
    private Instant statusAt;

    public Instant getStatusAt() {
        return statusAt;
    }

    public void setStatusAt(Instant statusAt) {
        this.statusAt = statusAt;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String id) {
        this.id = id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Job getData() {
        return data;
    }

    public void setData(Job data) {
        this.data = data;
    }

    public Instant getCreateId() {
        return createId;
    }

    public void setCreateId(Instant createdAt) {
        this.createId = createdAt;
    }
}
