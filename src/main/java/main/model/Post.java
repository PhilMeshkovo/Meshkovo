package main.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "posts")
@Data
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(nullable = false)
  private String text;

  @Column(nullable = false)
  private String name;

  @Column
  private String phone;

  @Column(nullable = false)
  private LocalDateTime time;

  @Column
  private String ip;

  @Column(nullable = false)
  private String status;
}
