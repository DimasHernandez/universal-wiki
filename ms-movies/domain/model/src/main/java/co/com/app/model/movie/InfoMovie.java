package co.com.app.model.movie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder()
public class InfoMovie {

    private Integer id;

    private List<Movie> results;

    private Integer totalPages;

    private Integer totalResults;
}
