package wendergalan.github.io.desafiosambatech.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "media", schema = "public")
public class Media implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    @NotNull
    @EqualsAndHashCode.Include
    private Integer id;

    @NotNull
    @Column(name = "nome", nullable = false, length = 512)
    @Length(max = 512)
    private String nome;

    @Column(name = "url", length = 512)
    @Length(max = 512)
    private String url;

    @Column(name = "duracao")
    private Integer duracao;

    @Column(name = "data_upload", nullable = false)
    @NotNull
    private LocalDate dataUpload;

    @Column(name = "deleted", nullable = false)
    @NotNull
    private boolean deleted;
}