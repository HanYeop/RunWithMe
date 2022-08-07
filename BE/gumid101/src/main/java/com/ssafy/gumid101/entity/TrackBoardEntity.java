package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 추천 경로게시판
 * @author start
 *
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="t_track_board")
@EntityListeners(AuditingEntityListener.class)
public class TrackBoardEntity {

	@Id
	@Column(name="track_board_seq")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long trackBoardSeq;
	
	@Column(name="track_board_content")
	private String trackBoardContent;

	@JoinColumn(name="run_record_seq")
	@ManyToOne
	private RunRecordEntity runRecordEntity;
	
	@JoinColumn(name="run_record_img_seq")
	@ManyToOne
	private ImageFileEntity trackBoardImageEntity;
	
	@Column(nullable = true, name="track_board_hard_point")
	private Integer trackBoardHardPoint;
	
	@Column(nullable = true, name="track_board_enviroment_point")
	private Integer trackBoardEnviromentPoint;
	
	@CreatedDate
	@Column(nullable = false, name="track_board_reg_time")
	private LocalDateTime trackBoardRegTime;
}
