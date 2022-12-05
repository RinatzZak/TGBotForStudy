package org.rinatzzak.dao;

import org.rinatzzak.entity.AppPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppPhotoDao extends JpaRepository<AppPhoto, Long> {
}
