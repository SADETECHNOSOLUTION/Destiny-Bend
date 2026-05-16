package com.sadetech.profession.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalInformationDTO {

    private String additionalInfoJson;
    private MultipartFile portfolioAndWorkSample;

}
