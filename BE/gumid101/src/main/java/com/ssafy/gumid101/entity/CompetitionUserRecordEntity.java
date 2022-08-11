package com.ssafy.gumid101.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name="t_competition_user_record")
@Builder
@Entity
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class CompetitionUserRecordEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "competition_user_record_seq")
	private Long competitionUserRecordSeq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_seq")
	private UserEntity userEntity;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "competition_seq")
	private CompetitionEntity competitionEntity;
	
	@Column(nullable = false, name = "competition_distance")
	private Integer competitionDistance;

	@Column(nullable = false, name = "competition_time")
	private Integer competitionTime;
	
	@PrePersist
	public void setting() {
		this.competitionDistance = 0;
		this.competitionTime = 0;
	}
	
}
