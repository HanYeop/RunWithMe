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
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.ssafy.gumid101.user.Role;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	private Long id;

	// 이미지 테이블이랑 관꼐 만들어야함
	@Column(nullable = true, name = "user_nickname")
	private String nickName;

	@Column(nullable = false, name = "user_email")
	private String email;

	@Column(nullable = true, name = "user_height")
	private Integer height;

	@Column(nullable = true, name = "user_weight")
	private Integer weight;

	@Column(name = "user_point")
	private Integer point;

	@Column(name = "user_fcm_token")
	private String fcmToken;

	@Column(name = "user_delete_yn", columnDefinition = "varchar(10) default 'N'")
	private String userState;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, name = "user_roll")
	private Role role;

	@CreatedDate
	@Column(name = "user_reg_time")
	private LocalDateTime regTime;

	@JoinColumn(name = "img_seq")
	@OneToOne
	private ImageFileEntity imageFile;

	
	@OneToMany(mappedBy = "userEntity")
	private List<RunRecordEntity> runRecordEntity;
	
	@OneToMany(mappedBy = "userEntity")
	private List<UserCrewJoinEntity> userCrewJoins;
	
	@OneToMany(mappedBy = "userEntity")
	private List<CrewBoardEntity> crewBoards;
	
	@OneToMany(mappedBy = "userEntity")
	private List<AchievementCompleteEntity> achievementCompleteList;
	
	
	
	@OneToMany(mappedBy = "userEntity")
	private List<CrewTotalRecordEntity> crewTotalRecordEntity;
	
	@Builder
	public UserEntity(String email, Role role) {
		this.email = email;
		this.role = role;
	}

	public String getRoleKey() {
		return this.role.getKey();
	}
}