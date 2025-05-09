package com.company.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedQuery(
        name = "BinResource.findAllByBinNum",
        query = "FROM BinResource b WHERE b.binNum= :binNum"
)
@Entity
@Table(name = "bin_resources")
@NoArgsConstructor
@Getter
@Setter
public class BinResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "low_account_range")
    private Integer lowAccountRange;

    @Column(name = "high_account_range")
    private Integer highAccountRange;

    @Column(name = "bin_num")
    private Integer binNum;

    @Column(name = "bin_length")
    private Integer binLength;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "country_alpha_3")
    private String countryAlpha3;

}
