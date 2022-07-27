package com.ssafy.gumid101.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ssafy.gumid101.user.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@Builder
@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_user", uniqueConstraints = {
		@UniqueConstraint(name = "nickname_unique_contraint", columnNames = { "user_nickname" }) })
@NoArgsConstructor
public class UserEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_seq")
	private Long userSeq;

	@Column(nullable = true, name = "user_nickname")
	private String nickName;

	@Column(nullable = false, name = "user_email")
	private String email;

	@Column(nullable = true, name = "user_height")
	private Integer height;

	@Column(nullable = true, name = "user_weight")
	private Integer weight;

	@Column(nullable = false, name = "user_point", columnDefinition = "Int default 0")
	private Integer point;

	@Column(nullable = true, name = "user_fcm_token")
	private String fcmToken;

	@Column(nullable = false, name = "user_delete_yn", columnDefinition = "varchar(10) default 'N'")
	private String userState;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "user_roll",columnDefinition = "varchar(10) default 'USER'")
	private Role role;

	@CreatedDate
	@Column(name = "user_reg_time")
	private LocalDateTime regTime;

	@JoinColumn(name = "img_seq")
	@OneToOne
	private ImageFileEntity imageFile;
	
	@OneToMany(mappedBy = "userEntity")
	private List<AchievementCompleteEntity> achievementCompleteEntitys;
	
	@OneToMany(mappedBy = "userEntity")
	private List<CrewTotalRecordEntity> crewTotalRecordEntitys;
	
	@OneToMany(mappedBy = "userEntity")
	private List<RunRecordEntity> runRecordEntitys;
	
	@OneToMany(mappedBy = "userEntity")
	private List<UserCrewJoinEntity> userCrewJoinEntitys;
	
	@OneToMany(mappedBy = "userEntity")
	private List<CrewBoardEntity> crewBoardEntitys;
	
	@OneToMany(mappedBy = "userReporterEntity")
	private List<ReportEntity> reportEntitys;
	
	@OneToMany(mappedBy = "userEntity")
	private List<QuestionEntity> questionEntitys;
	
	

	@PrePersist
	public void setting() {
		this.point = 0;
		this.userState = "N";
		this.role = Role.USER;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}
}