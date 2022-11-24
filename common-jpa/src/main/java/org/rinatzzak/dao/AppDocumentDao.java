package org.rinatzzak.dao;

import org.rinatzzak.entity.enums.AppDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppDocumentDao extends JpaRepository<AppDocument, Long> {
}
