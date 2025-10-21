package co.com.app.model.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder()
@ToString
public class InfoMovie {

    private Integer page;

    private List<Movie> movies;

    private Integer totalPages;

    private Integer totalMovies;

    private Integer total;
}
