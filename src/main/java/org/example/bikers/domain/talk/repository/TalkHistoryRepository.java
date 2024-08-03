package org.example.bikers.domain.talk.repository;

import org.example.bikers.domain.talk.entity.TalkHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TalkHistoryRepository extends JpaRepository<TalkHistory, Long>,
    TalkHistoryRepositoryCustom {

}
