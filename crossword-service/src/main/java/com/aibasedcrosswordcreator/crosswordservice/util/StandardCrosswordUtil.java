package com.aibasedcrosswordcreator.crosswordservice.util;

import com.aibasedcrosswordcreator.crosswordservice.model.StandardCrossword;
import com.aibasedcrosswordcreator.crosswordservice.model.Language;
import com.aibasedcrosswordcreator.crosswordservice.model.Model;
import com.aibasedcrosswordcreator.crosswordservice.model.Provider;
import org.springframework.data.jpa.domain.Specification;

public class StandardCrosswordUtil {
    public static Specification<StandardCrossword> getCreatorSpecification(String creator) {
        if (creator != null && creator.equals("null")) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isNull(root.get("creator"));
        } else {
            return (root, query, criteriaBuilder) ->
                    creator != null && creator.isEmpty() ? null : criteriaBuilder.equal(root.get("creator"), creator);
        }
    }

    public static Specification<StandardCrossword> getLanguageSpecification(Language language) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("language"), language);
    }

    public static Specification<StandardCrossword> getProviderSpecification(Provider provider) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("providerModel").get("provider"), provider);
    }

    public static Specification<StandardCrossword> getModelSpecification(Model model) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("providerModel").get("model"), model);
    }

    public static Specification<StandardCrossword> getHeightSpecification(int height) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("height"), height);
    }

    public static Specification<StandardCrossword> getWidthSpecification(int width) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("width"), width);
    }
}