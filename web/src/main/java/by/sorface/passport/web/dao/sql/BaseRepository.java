package by.sorface.passport.web.dao.sql;

import by.sorface.passport.web.dao.models.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.UUID;

/**
 * Базовый JPA репозиторий для доменной модели
 *
 * @param <T> сущность доменной модели
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity> extends JpaRepository<T, UUID> {
}
