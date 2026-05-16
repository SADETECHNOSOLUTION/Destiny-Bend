package com.sadetech.profession.model.sub_model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "physical_attribute")
public class PhysicalAttribute {
    @Id
    private String id;
    private Long userId;
    private String height;
    private String weight;
    private String bodyType;
    private String hairColor;
    private String hairLength;
    private String eyeColor;
    private String skinTone;
    private String facialHair;
    private boolean tattoos;
    private String piercings;
    private String distinctFeatures;
    private int shoeSize;
    private String clothingSize;
    private String build;
    private String voiceType;
}
