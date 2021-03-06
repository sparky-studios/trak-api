package com.sparkystudios.traklibrary.game.repository.specification;

import com.sparkystudios.traklibrary.game.domain.Game;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.StartingWithIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;

@And({
        @Spec(path = "id", spec = Equal.class),
        @Spec(path = "title", spec = StartingWithIgnoreCase.class),
        @Spec(path = "ageRating", params = "age-rating", spec = Equal.class),
        @Spec(path = "slug", spec = StartingWithIgnoreCase.class)
})
public interface GameSpecification extends Specification<Game> {
}
