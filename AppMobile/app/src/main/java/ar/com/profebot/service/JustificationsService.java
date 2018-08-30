package ar.com.profebot.service;

import android.content.Context;
import android.widget.Toast;

import com.profebot.activities.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.profebot.intelligent.module.IAModuleClient;
import ar.com.profebot.parser.container.Tree;
import ar.com.profebot.parser.exception.InvalidExpressionException;
import ar.com.profebot.parser.service.ParserService;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.*;
import de.uni_bielefeld.cebitec.mzurowie.pretty_formula.main.FormulaParser;

public class JustificationsService {

    public static Map<String, String> getCorrectJustificationsFrom(ChangeTypes source, Context context){
        Map<String, String> justifications = new HashMap<>();

        if(source.getDescrip().equals(SIMPLIFY_ARITHMETIC.getDescrip())){
            return createTextsFrom(context,
                    R.string.SIMPLIFY_ARITHMETIC_OPTION,
                    R.string.SIMPLIFY_ARITHMETIC_JUSTIFICATION,
                    R.string.SIMPLIFY_ARITHMETIC_SUMMARY);
        }

        if(source.getDescrip().equals(DIVISION_BY_NEGATIVE_ONE.getDescrip())){
            return createTextsFrom(context,
                    R.string.DIVISION_BY_NEGATIVE_ONE_OPTION,
                    R.string.DIVISION_BY_NEGATIVE_ONE_JUSTIFICATION,
                    R.string.DIVISION_BY_NEGATIVE_ONE_SUMMARY);
        }

        if(source.getDescrip().equals(DIVISION_BY_NEGATIVE_ONE.getDescrip())){
            return createTextsFrom(context,
                    R.string.DIVISION_BY_ONE_OPTION,
                    R.string.DIVISION_BY_ONE_JUSTIFICATION,
                    R.string.DIVISION_BY_ONE_SUMMARY);
        }

        if(source.getDescrip().equals(MULTIPLY_BY_ZERO.getDescrip())){
            return createTextsFrom(context,
                    R.string.MULTIPLY_BY_ZERO_OPTION,
                    R.string.MULTIPLY_BY_ZERO_JUSTIFICATION,
                    R.string.MULTIPLY_BY_ZERO_SUMMARY);
        }

        if(source.getDescrip().equals(REARRANGE_COEFF.getDescrip())
                || source.getDescrip().equals(REMOVE_MULTIPLYING_BY_NEGATIVE_ONE.getDescrip())
                || source.getDescrip().equals(REMOVE_MULTIPLYING_BY_ONE.getDescrip())
                || source.getDescrip().equals(ADD_COEFFICIENT_OF_ONE.getDescrip())
                || source.getDescrip().equals(UNARY_MINUS_TO_NEGATIVE_ONE.getDescrip())){
            return createTextsFrom(context,
                    R.string.REARRANGE_COEFF_OPTION,
                    R.string.REARRANGE_COEFF_JUSTIFICATION,
                    R.string.REARRANGE_COEFF_SUMMARY);
        }

        if(source.getDescrip().equals(REDUCE_EXPONENT_BY_ZERO.getDescrip())){
            return createTextsFrom(context,
                    R.string.REDUCE_EXPONENT_BY_ZERO_OPTION,
                    R.string.REDUCE_EXPONENT_BY_ZERO_JUSTIFICATION,
                    R.string.REDUCE_EXPONENT_BY_ZERO_SUMMARY);
        }

        if(source.getDescrip().equals(REDUCE_ZERO_NUMERATOR.getDescrip())){
            return createTextsFrom(context,
                    R.string.REDUCE_ZERO_NUMERATOR_OPTION,
                    R.string.REDUCE_ZERO_NUMERATOR_JUSTIFICATION,
                    R.string.REDUCE_ZERO_NUMERATOR_SUMMARY);
        }

        if(source.getDescrip().equals(REMOVE_ADDING_ZERO.getDescrip())){
            return createTextsFrom(context,
                    R.string.REMOVE_ADDING_ZERO_OPTION,
                    R.string.REMOVE_ADDING_ZERO_JUSTIFICATION,
                    R.string.REMOVE_ADDING_ZERO_SUMMARY);
        }

        if(source.getDescrip().equals(REMOVE_EXPONENT_BY_ONE.getDescrip())){
            return createTextsFrom(context,
                    R.string.REMOVE_EXPONENT_BY_ONE_OPTION,
                    R.string.REMOVE_EXPONENT_BY_ONE_JUSTIFICATION,
                    R.string.REMOVE_EXPONENT_BY_ONE_SUMMARY);
        }

        if(source.getDescrip().equals(REMOVE_EXPONENT_BASE_ONE.getDescrip())){
            return createTextsFrom(context,
                    R.string.REMOVE_EXPONENT_BASE_ONE_OPTION,
                    R.string.REMOVE_EXPONENT_BASE_ONE_JUSTIFICATION,
                    R.string.REMOVE_EXPONENT_BASE_ONE_SUMMARY);
        }

        if(source.getDescrip().equals(RESOLVE_DOUBLE_MINUS.getDescrip())){
            return createTextsFrom(context,
                    R.string.RESOLVE_DOUBLE_MINUS_OPTION,
                    R.string.RESOLVE_DOUBLE_MINUS_JUSTIFICATION,
                    R.string.RESOLVE_DOUBLE_MINUS_SUMMARY);
        }

        if(source.getDescrip().equals(COLLECT_AND_COMBINE_LIKE_TERMS.getDescrip())
                || source.getDescrip().equals(COLLECT_LIKE_TERMS.getDescrip())
                || source.getDescrip().equals(ADD_POLYNOMIAL_TERMS.getDescrip())
                || source.getDescrip().equals(GROUP_COEFFICIENTS.getDescrip())
                || source.getDescrip().equals(MULTIPLY_COEFFICIENTS.getDescrip())
                || source.getDescrip().equals(SIMPLIFY_TERMS.getDescrip())){
            return createTextsFrom(context,
                    R.string.COLLECT_AND_COMBINE_LIKE_TERMS_OPTION,
                    R.string.COLLECT_AND_COMBINE_LIKE_TERMS_JUSTIFICATION,
                    R.string.COLLECT_AND_COMBINE_LIKE_TERMS_SUMMARY);
        }

        if(source.getDescrip().equals(COLLECT_CONSTANT_EXPONENTS.getDescrip())
                || source.getDescrip().equals(COLLECT_POLYNOMIAL_EXPONENTS.getDescrip())){
            return createTextsFrom(context,
                    R.string.COLLECT_CONSTANT_EXPONENTS_OPTION,
                    R.string.COLLECT_CONSTANT_EXPONENTS_JUSTIFICATION,
                    R.string.COLLECT_CONSTANT_EXPONENTS_SUMMARY);
        }

        if(source.getDescrip().equals(ADD_EXPONENT_OF_ONE.getDescrip())){
            return createTextsFrom(context,
                    R.string.ADD_EXPONENT_OF_ONE_OPTION,
                    R.string.ADD_EXPONENT_OF_ONE_JUSTIFICATION,
                    R.string.ADD_EXPONENT_OF_ONE_SUMMARY);
        }

        if(source.getDescrip().equals(MULTIPLY_POLYNOMIAL_TERMS.getDescrip())){
            return createTextsFrom(context,
                    R.string.MULTIPLY_POLYNOMIAL_TERMS_OPTION,
                    R.string.MULTIPLY_POLYNOMIAL_TERMS_JUSTIFICATION,
                    R.string.MULTIPLY_POLYNOMIAL_TERMS_SUMMARY);
        }

        if(source.getDescrip().equals(BREAK_UP_FRACTION.getDescrip())){
            return createTextsFrom(context,
                    R.string.BREAK_UP_FRACTION_OPTION,
                    R.string.BREAK_UP_FRACTION_JUSTIFICATION,
                    R.string.BREAK_UP_FRACTION_SUMMARY);
        }

        if(source.getDescrip().equals(CANCEL_MINUSES.getDescrip())){
            return createTextsFrom(context,
                    R.string.CANCEL_MINUSES_OPTION,
                    R.string.CANCEL_MINUSES_JUSTIFICATION,
                    R.string.CANCEL_MINUSES_SUMMARY);
        }

        if(source.getDescrip().equals(CANCEL_TERMS.getDescrip())){
            return createTextsFrom(context,
                    R.string.CANCEL_TERMS_OPTION,
                    R.string.CANCEL_TERMS_JUSTIFICATION,
                    R.string.CANCEL_TERMS_SUMMARY);
        }

        if(source.getDescrip().equals(SIMPLIFY_FRACTION.getDescrip())){
            return createTextsFrom(context,
                    R.string.SIMPLIFY_FRACTION_OPTION,
                    R.string.SIMPLIFY_FRACTION_JUSTIFICATION,
                    R.string.SIMPLIFY_FRACTION_SUMMARY);
        }

        if(source.getDescrip().equals(SIMPLIFY_SIGNS.getDescrip())){
            return createTextsFrom(context,
                    R.string.SIMPLIFY_SIGNS_OPTION,
                    R.string.SIMPLIFY_SIGNS_JUSTIFICATION,
                    R.string.SIMPLIFY_SIGNS_SUMMARY);
        }

        // TODO: pending: FIND_GCD, CANCEL_GCD, CONVERT_MIXED_NUMBER_TO_IMPROPER_FRACTION, IMPROPER_FRACTION_NUMERATOR, COMBINE_NUMERATORS

        if(source.getDescrip().equals(ADD_FRACTIONS.getDescrip())
                || source.getDescrip().equals(ADD_NUMERATORS.getDescrip())
                || source.getDescrip().equals(COMBINE_NUMERATORS.getDescrip())){
            return createTextsFrom(context,
                    R.string.ADD_FRACTIONS_OPTION,
                    R.string.ADD_FRACTIONS_JUSTIFICATION,
                    R.string.ADD_FRACTIONS_SUMMARY);
        }

        if(source.getDescrip().equals(COMMON_DENOMINATOR.getDescrip())){
            return createTextsFrom(context,
                    R.string.COMMON_DENOMINATOR_OPTION,
                    R.string.COMMON_DENOMINATOR_JUSTIFICATION,
                    R.string.COMMON_DENOMINATOR_SUMMARY);
        }

        if(source.getDescrip().equals(CONVERT_INTEGER_TO_FRACTION.getDescrip())){
            return createTextsFrom(context,
                    R.string.CONVERT_INTEGER_TO_FRACTION_OPTION,
                    R.string.CONVERT_INTEGER_TO_FRACTION_JUSTIFICATION,
                    R.string.CONVERT_INTEGER_TO_FRACTION_SUMMARY);
        }

        if(source.getDescrip().equals(DIVIDE_FRACTION_FOR_ADDITION.getDescrip())){
            return createTextsFrom(context,
                    R.string.DIVIDE_FRACTION_FOR_ADDITION_OPTION,
                    R.string.DIVIDE_FRACTION_FOR_ADDITION_JUSTIFICATION,
                    R.string.DIVIDE_FRACTION_FOR_ADDITION_SUMMARY);
        }

        if(source.getDescrip().equals(MULTIPLY_DENOMINATORS.getDescrip())){
            return createTextsFrom(context,
                    R.string.MULTIPLY_DENOMINATORS_OPTION,
                    R.string.MULTIPLY_DENOMINATORS_JUSTIFICATION,
                    R.string.MULTIPLY_DENOMINATORS_SUMMARY);
        }

        if(source.getDescrip().equals(MULTIPLY_NUMERATORS.getDescrip())){
            return createTextsFrom(context,
                    R.string.MULTIPLY_NUMERATORS_OPTION,
                    R.string.MULTIPLY_NUMERATORS_JUSTIFICATION,
                    R.string.MULTIPLY_NUMERATORS_SUMMARY);
        }

        if(source.getDescrip().equals(MULTIPLY_FRACTIONS.getDescrip())){
            return createTextsFrom(context,
                    R.string.MULTIPLY_FRACTIONS_OPTION,
                    R.string.MULTIPLY_FRACTIONS_JUSTIFICATION,
                    R.string.MULTIPLY_FRACTIONS_SUMMARY);
        }

        if(source.getDescrip().equals(SIMPLIFY_DIVISION.getDescrip())){
            return createTextsFrom(context,
                    R.string.SIMPLIFY_DIVISION_OPTION,
                    R.string.SIMPLIFY_DIVISION_JUSTIFICATION,
                    R.string.SIMPLIFY_DIVISION_SUMMARY);
        }

        if(source.getDescrip().equals(MULTIPLY_BY_INVERSE.getDescrip())){
            return createTextsFrom(context,
                    R.string.MULTIPLY_BY_INVERSE_OPTION,
                    R.string.MULTIPLY_BY_INVERSE_JUSTIFICATION,
                    R.string.MULTIPLY_BY_INVERSE_SUMMARY);
        }

        if(source.getDescrip().equals(DISTRIBUTE.getDescrip())
                || source.getDescrip().equals(DISTRIBUTE_NEGATIVE_ONE.getDescrip())){
            return createTextsFrom(context,
                    R.string.DISTRIBUTE_OPTION,
                    R.string.DISTRIBUTE_JUSTIFICATION,
                    R.string.DISTRIBUTE_SUMMARY);
        }

        if(source.getDescrip().equals(EXPAND_EXPONENT.getDescrip())){
            return createTextsFrom(context,
                    R.string.EXPAND_EXPONENT_OPTION,
                    R.string.EXPAND_EXPONENT_JUSTIFICATION,
                    R.string.EXPAND_EXPONENT_SUMMARY);
        }

        if(source.getDescrip().equals(ABSOLUTE_VALUE.getDescrip())){
            return createTextsFrom(context,
                    R.string.ABSOLUTE_VALUE_OPTION,
                    R.string.ABSOLUTE_VALUE_JUSTIFICATION,
                    R.string.ABSOLUTE_VALUE_SUMMARY);
        }


        if(source.getDescrip().equals(CANCEL_EXPONENT.getDescrip())
                || source.getDescrip().equals(CANCEL_EXPONENT_AND_ROOT.getDescrip())
                || source.getDescrip().equals(CANCEL_ROOT.getDescrip())
                || source.getDescrip().equals(EVALUATE_DISTRIBUTED_NTH_ROOT.getDescrip())
                || source.getDescrip().equals(NTH_ROOT_VALUE.getDescrip())){
            return createTextsFrom(context,
                    R.string.CANCEL_EXPONENT_OPTION,
                    R.string.CANCEL_EXPONENT_JUSTIFICATION,
                    R.string.CANCEL_EXPONENT_SUMMARY);
        }

        if(source.getDescrip().equals(COMBINE_UNDER_ROOT.getDescrip())){
            return createTextsFrom(context,
                    R.string.COMBINE_UNDER_ROOT_OPTION,
                    R.string.COMBINE_UNDER_ROOT_JUSTIFICATION,
                    R.string.COMBINE_UNDER_ROOT_SUMMARY);
        }

        if(source.getDescrip().equals(CONVERT_MULTIPLICATION_TO_EXPONENT.getDescrip())){
            return createTextsFrom(context,
                    R.string.CONVERT_MULTIPLICATION_TO_EXPONENT_OPTION,
                    R.string.CONVERT_MULTIPLICATION_TO_EXPONENT_JUSTIFICATION,
                    R.string.CONVERT_MULTIPLICATION_TO_EXPONENT_SUMMARY);
        }

        if(source.getDescrip().equals(DISTRIBUTE_NTH_ROOT.getDescrip())){
            return createTextsFrom(context,
                    R.string.DISTRIBUTE_NTH_ROOT_OPTION,
                    R.string.DISTRIBUTE_NTH_ROOT_JUSTIFICATION,
                    R.string.DISTRIBUTE_NTH_ROOT_SUMMARY);
        }

        if(source.getDescrip().equals(FACTOR_INTO_PRIMES.getDescrip())){
            return createTextsFrom(context,
                    R.string.FACTOR_INTO_PRIMES_OPTION,
                    R.string.FACTOR_INTO_PRIMES_JUSTIFICATION,
                    R.string.FACTOR_INTO_PRIMES_SUMMARY);
        }

        // TODO: GROUP_TERMS_BY_ROOT

        if(source.getDescrip().equals(ADD_NTH_ROOTS.getDescrip())){
            return createTextsFrom(context,
                    R.string.ADD_NTH_ROOTS_OPTION,
                    R.string.ADD_NTH_ROOTS_JUSTIFICATION,
                    R.string.ADD_NTH_ROOTS_SUMMARY);
        }

        if(source.getDescrip().equals(MULTIPLY_NTH_ROOTS.getDescrip())){
            return createTextsFrom(context,
                    R.string.MULTIPLY_NTH_ROOTS_OPTION,
                    R.string.MULTIPLY_NTH_ROOTS_JUSTIFICATION,
                    R.string.MULTIPLY_NTH_ROOTS_SUMMARY);
        }

        if(source.getDescrip().equals(ADD_TO_BOTH_SIDES.getDescrip())){
            return createTextsFrom(context,
                    R.string.ADD_TO_BOTH_SIDES_OPTION,
                    R.string.ADD_TO_BOTH_SIDES_JUSTIFICATION,
                    R.string.ADD_TO_BOTH_SIDES_SUMMARY);
        }

        if(source.getDescrip().equals(DIVIDE_FROM_BOTH_SIDES.getDescrip())){
            return createTextsFrom(context,
                    R.string.DIVIDE_FROM_BOTH_SIDES_OPTION,
                    R.string.DIVIDE_FROM_BOTH_SIDES_JUSTIFICATION,
                    R.string.DIVIDE_FROM_BOTH_SIDES_SUMMARY);
        }

        if(source.getDescrip().equals(MULTIPLY_BOTH_SIDES_BY_INVERSE_FRACTION.getDescrip())
                || source.getDescrip().equals(MULTIPLY_BOTH_SIDES_BY_NEGATIVE_ONE.getDescrip())
                || source.getDescrip().equals(MULTIPLY_TO_BOTH_SIDES.getDescrip())){
            return createTextsFrom(context,
                    R.string.MULTIPLY_BOTH_SIDES_BY_INVERSE_FRACTION_OPTION,
                    R.string.MULTIPLY_BOTH_SIDES_BY_INVERSE_FRACTION_JUSTIFICATION,
                    R.string.MULTIPLY_BOTH_SIDES_BY_INVERSE_FRACTION_SUMMARY);
        }

        if(source.getDescrip().equals(SIMPLIFY_LEFT_SIDE.getDescrip())){
            return createTextsFrom(context,
                    R.string.SIMPLIFY_LEFT_SIDE_OPTION,
                    R.string.SIMPLIFY_LEFT_SIDE_JUSTIFICATION,
                    R.string.SIMPLIFY_LEFT_SIDE_SUMMARY);
        }

        if(source.getDescrip().equals(SIMPLIFY_RIGHT_SIDE.getDescrip())){
            return createTextsFrom(context,
                    R.string.SIMPLIFY_RIGHT_SIDE_OPTION,
                    R.string.SIMPLIFY_RIGHT_SIDE_JUSTIFICATION,
                    R.string.SIMPLIFY_RIGHT_SIDE_SUMMARY);
        }

        if(source.getDescrip().equals(SUBTRACT_FROM_BOTH_SIDES.getDescrip())){
            return createTextsFrom(context,
                    R.string.SUBTRACT_FROM_BOTH_SIDES_OPTION,
                    R.string.SUBTRACT_FROM_BOTH_SIDES_JUSTIFICATION,
                    R.string.SUBTRACT_FROM_BOTH_SIDES_SUMMARY);
        }


        // TODO: SWAP_SIDES

        if(source.getDescrip().equals(FIND_ROOTS.getDescrip())){
            return createTextsFrom(context,
                    R.string.FIND_ROOTS_OPTION,
                    R.string.FIND_ROOTS_JUSTIFICATION,
                    R.string.FIND_ROOTS_SUMMARY);
        }

        if(source.getDescrip().equals(STATEMENT_IS_TRUE.getDescrip())){
            return createTextsFrom(context,
                    R.string.STATEMENT_IS_TRUE_OPTION,
                    R.string.STATEMENT_IS_TRUE_JUSTIFICATION,
                    R.string.STATEMENT_IS_TRUE_SUMMARY);
        }

        if(source.getDescrip().equals(STATEMENT_IS_FALSE.getDescrip())){
            return createTextsFrom(context,
                    R.string.STATEMENT_IS_FALSE_OPTION,
                    R.string.STATEMENT_IS_FALSE_JUSTIFICATION,
                    R.string.STATEMENT_IS_FALSE_SUMMARY);
        }

        if(source.getDescrip().equals(FACTOR_SYMBOL.getDescrip())){
            return createTextsFrom(context,
                    R.string.FACTOR_SYMBOL_OPTION,
                    R.string.FACTOR_SYMBOL_JUSTIFICATION,
                    R.string.FACTOR_SYMBOL_SUMMARY);
        }

        if(source.getDescrip().equals(FACTOR_DIFFERENCE_OF_SQUARES.getDescrip())){
            return createTextsFrom(context,
                    R.string.FACTOR_DIFFERENCE_OF_SQUARES_OPTION,
                    R.string.FACTOR_DIFFERENCE_OF_SQUARES_JUSTIFICATION,
                    R.string.FACTOR_DIFFERENCE_OF_SQUARES_SUMMARY);
        }

        if(source.getDescrip().equals(FACTOR_PERFECT_SQUARE.getDescrip())){
            return createTextsFrom(context,
                    R.string.FACTOR_PERFECT_SQUARE_OPTION,
                    R.string.FACTOR_PERFECT_SQUARE_JUSTIFICATION,
                    R.string.FACTOR_PERFECT_SQUARE_SUMMARY);
        }

        if(source.getDescrip().equals(FACTOR_SUM_PRODUCT_RULE.getDescrip())){
            return createTextsFrom(context,
                    R.string.FACTOR_SUM_PRODUCT_RULE_OPTION,
                    R.string.FACTOR_SUM_PRODUCT_RULE_JUSTIFICATION,
                    R.string.FACTOR_SUM_PRODUCT_RULE_SUMMARY);
        }

        // TODO: BREAK_UP_TERM

        return justifications;
    }

    private static Map<String, String> createTextsFrom(Context context, int optionId, int justificationId, int summaryId){
        Map<String, String> justifications = new HashMap<>();
        justifications.put("option", context.getString(optionId));
        justifications.put("correctOptionJustification", context.getString(justificationId));
        justifications.put("summary", context.getString(summaryId));
        return justifications;
    }
}
