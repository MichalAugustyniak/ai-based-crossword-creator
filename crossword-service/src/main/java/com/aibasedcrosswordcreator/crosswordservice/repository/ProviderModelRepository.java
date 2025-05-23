package com.aibasedcrosswordcreator.crosswordservice.repository;

import com.aibasedcrosswordcreator.crosswordservice.model.Model;
import com.aibasedcrosswordcreator.crosswordservice.model.Provider;
import com.aibasedcrosswordcreator.crosswordservice.model.ProviderModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProviderModelRepository extends JpaRepository<ProviderModel, Long> {
    Optional<ProviderModel> findByProviderAndModel(Provider provider, Model model);
}
