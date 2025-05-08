package com.company.application;

import com.company.application.dto.TransactionAssessment;
import com.company.interfaces.rest.dto.TransactionDto;

/**
 * Risk factors:
 * amount:
 * bin data:
 * transaction type:
 *
 * equation: amount_ration * amount + bin_ratio * ( exist-1 or not-0) + country_ration * (bin country-1 different-0) / number * 100
 *  transaction type | amount_ratio | bin_data_ratio
 * DEPOSIT | 0 | 1
 * WITHDRAW | 0,5 | 1
 *
 *
 * +
 *
 *
 */
public interface FraudTransactionAssessmentService {

    TransactionAssessment processAssessment(TransactionDto transaction);

    String getType();
}
