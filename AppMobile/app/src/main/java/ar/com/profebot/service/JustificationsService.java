package ar.com.profebot.service;

import android.content.Context;

import com.profebot.activities.R;

import java.util.HashMap;
import java.util.Map;

import ar.com.profebot.activities.SolveEquationActivity;
import ar.com.profebot.activities.SolvePolynomialActivity;
import ar.com.profebot.resolutor.container.InvalidStep;

import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.ABSOLUTE_VALUE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.ADD_COEFFICIENT_OF_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.ADD_EXPONENT_OF_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.ADD_FRACTIONS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.ADD_NTH_ROOTS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.ADD_NUMERATORS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.ADD_POLYNOMIAL_TERMS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.ADD_TO_BOTH_SIDES;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.BREAK_UP_FRACTION;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.CANCEL_EXPONENT;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.CANCEL_EXPONENT_AND_ROOT;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.CANCEL_MINUSES;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.CANCEL_ROOT;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.CANCEL_TERMS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.COLLECT_AND_COMBINE_LIKE_TERMS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.COLLECT_CONSTANT_EXPONENTS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.COLLECT_LIKE_TERMS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.COLLECT_POLYNOMIAL_EXPONENTS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.COMBINE_NUMERATORS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.COMBINE_UNDER_ROOT;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.COMMON_DENOMINATOR;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.CONVERT_INTEGER_TO_FRACTION;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.CONVERT_MULTIPLICATION_TO_EXPONENT;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.DISTRIBUTE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.DISTRIBUTE_NEGATIVE_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.DISTRIBUTE_NTH_ROOT;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.DIVIDE_FRACTION_FOR_ADDITION;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.DIVIDE_FROM_BOTH_SIDES;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.DIVISION_BY_NEGATIVE_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.EVALUATE_DISTRIBUTED_NTH_ROOT;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.EXPAND_EXPONENT;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.FACTOR_DIFFERENCE_OF_SQUARES;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.FACTOR_INTO_PRIMES;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.FACTOR_PERFECT_SQUARE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.FACTOR_SUM_PRODUCT_RULE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.FACTOR_SYMBOL;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.FIND_ROOTS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.GROUP_COEFFICIENTS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_BOTH_SIDES_BY_INVERSE_FRACTION;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_BOTH_SIDES_BY_NEGATIVE_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_BY_INVERSE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_BY_ZERO;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_COEFFICIENTS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_DENOMINATORS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_FRACTIONS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_NTH_ROOTS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_NUMERATORS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_POLYNOMIAL_TERMS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.MULTIPLY_TO_BOTH_SIDES;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.NTH_ROOT_VALUE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.REARRANGE_COEFF;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.REDUCE_EXPONENT_BY_ZERO;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.REDUCE_ZERO_NUMERATOR;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.REMOVE_ADDING_ZERO;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.REMOVE_EXPONENT_BASE_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.REMOVE_EXPONENT_BY_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_NEGATIVE_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.REMOVE_MULTIPLYING_BY_ONE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.RESOLVE_DOUBLE_MINUS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.SIMPLIFY_ARITHMETIC;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.SIMPLIFY_DIVISION;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.SIMPLIFY_FRACTION;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.SIMPLIFY_LEFT_SIDE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.SIMPLIFY_RIGHT_SIDE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.SIMPLIFY_SIGNS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.SIMPLIFY_TERMS;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.STATEMENT_IS_FALSE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.STATEMENT_IS_TRUE;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.SUBTRACT_FROM_BOTH_SIDES;
import static ar.com.profebot.resolutor.container.NodeStatus.ChangeTypes.UNARY_MINUS_TO_NEGATIVE_ONE;

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
        justifications.put("option", context.getString(optionId).replace("/comparador/", textOfComparator(ExpressionsManager.comparatorOperator)));
        justifications.put("correctOptionJustification", context.getString(justificationId).replace("/comparador/", textOfComparator(ExpressionsManager.comparatorOperator)));
        justifications.put("summary", context.getString(summaryId));
        return justifications;
    }

    public static String textOfComparator(String comparator){
        switch (comparator){
            case ">":
                return "mayor";
            case "<":
                return "menor";
            case ">=":
                return "mayor o igual";
            case "<=":
                return "menor o igual";
            case "=":
                return "igual";
            default:
                return "error";
        }
    }

    private static Map<String, String> createTextsFrom(Context context, int optionId, int justificationId){
        Map<String, String> justifications = new HashMap<>();
        justifications.put("option", context.getString(optionId).replace("/comparador/", textOfComparator(ExpressionsManager.comparatorOperator)));
        justifications.put("incorrectOptionJustification", context.getString(justificationId).replace("/comparador/", textOfComparator(ExpressionsManager.comparatorOperator)));
        return justifications;
    }

    public static Map<String, String> getIncorrectJustificationsFrom(InvalidStep.InvalidTypes source, Context context) {
        switch(source){
            // Tipo 1 - bullet 1
            case PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_SUMA_COMO_SUMA:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_SUMA_COMO_SUMA_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_SUMA_COMO_SUMA_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RESTA_COMO_RESTA:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RESTA_COMO_RESTA_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RESTA_COMO_RESTA_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_MULTIPLICACION_COMO_MULTIPLICACION:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_MULTIPLICACION_COMO_MULTIPLICACION_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_MULTIPLICACION_COMO_MULTIPLICACION_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_DIVISION_COMO_DIVISION:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_DIVISION_COMO_DIVISION_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_DIVISION_COMO_DIVISION_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_POTENCIA_COMO_POTENCIA:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_POTENCIA_COMO_POTENCIA_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_POTENCIA_COMO_POTENCIA_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RAIZ_COMO_RAIZ:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RAIZ_COMO_RAIZ_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RAIZ_COMO_RAIZ_JUSTIFICATION);

            // Tipo 1 - bullet 2 - al menos un ancestro de distinto tipo

            case PASAJE_TERMINO_DE_MULTIPLICACION_COMO_DIVISION_SIENDO_TERMINO_DE_SUMATORIA:
                return createTextsFrom(context,
                        R.string.PASAJE_TERMINO_DE_MULTIPLICACION_COMO_DIVISION_SIENDO_TERMINO_DE_SUMATORIA_OPTION,
                        R.string.PASAJE_TERMINO_DE_MULTIPLICACION_COMO_DIVISION_SIENDO_TERMINO_DE_SUMATORIA_JUSTIFICATION);

            case PASAJE_TERMINO_DE_DIVISION_COMO_MULTIPLICACION_SIENDO_TERMINO_DE_SUMATORIA:
                return createTextsFrom(context,
                        R.string.PASAJE_TERMINO_DE_DIVISION_COMO_MULTIPLICACION_SIENDO_TERMINO_DE_SUMATORIA_OPTION,
                        R.string.PASAJE_TERMINO_DE_DIVISION_COMO_MULTIPLICACION_SIENDO_TERMINO_DE_SUMATORIA_JUSTIFICATION);

            case PASAJE_TERMINO_DE_SUMA_COMO_RESTA_SIENDO_TERMINO_MUTIPLICATIVO:
                return createTextsFrom(context,
                        R.string.PASAJE_TERMINO_DE_SUMA_COMO_RESTA_SIENDO_TERMINO_MUTIPLICATIVO_OPTION,
                        R.string.PASAJE_TERMINO_DE_SUMA_COMO_RESTA_SIENDO_TERMINO_MUTIPLICATIVO_JUSTIFICATION);

            case PASAJE_TERMINO_DE_RESTA_COMO_SUMA_SIENDO_TERMINO_MUTIPLICATIVO:
                return createTextsFrom(context,
                        R.string.PASAJE_TERMINO_DE_RESTA_COMO_SUMA_SIENDO_TERMINO_MUTIPLICATIVO_OPTION,
                        R.string.PASAJE_TERMINO_DE_RESTA_COMO_SUMA_SIENDO_TERMINO_MUTIPLICATIVO_JUSTIFICATION);

            case PASAJE_TERMINO_DE_RAIZ_COMO_POTENCIA:
                return createTextsFrom(context,
                        R.string.PASAJE_TERMINO_DE_RAIZ_COMO_POTENCIA_OPTION,
                        R.string.PASAJE_TERMINO_DE_RAIZ_COMO_POTENCIA_JUSTIFICATION);

            case PASAJE_TERMINO_DE_POTENCIA_COMO_RAIZ:
                return createTextsFrom(context,
                        R.string.PASAJE_TERMINO_DE_POTENCIA_COMO_RAIZ_OPTION,
                        R.string.PASAJE_TERMINO_DE_POTENCIA_COMO_RAIZ_JUSTIFICATION);

            // Tipo 1 - bullet 2 - todos los ancestros del mismo tipo

            case PASAJE_DE_TERMINO_DE_DESCENDENCIA_SUMA_COMO_SUMA:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_SUMA_COMO_SUMA_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_SUMA_COMO_SUMA_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_DESCENDENCIA_RESTA_COMO_RESTA:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RESTA_COMO_RESTA_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RESTA_COMO_RESTA_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_DESCENDENCIA_MULTIPLICACION_COMO_MULTIPLICACION:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_MULTIPLICACION_COMO_MULTIPLICACION_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_MULTIPLICACION_COMO_MULTIPLICACION_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_DESCENDENCIA_DIVISION_COMO_DIVISION:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_DIVISION_COMO_DIVISION_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_DIVISION_COMO_DIVISION_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_DESCENDENCIA_POTENCIA_COMO_POTENCIA:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_POTENCIA_COMO_POTENCIA_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_POTENCIA_COMO_POTENCIA_JUSTIFICATION);

            case PASAJE_DE_TERMINO_DE_DESCENDENCIA_RAIZ_COMO_RAIZ:
                return createTextsFrom(context,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RAIZ_COMO_RAIZ_OPTION,
                        R.string.PASAJE_DE_TERMINO_DE_PRIMER_ANCESTRO_RAIZ_COMO_RAIZ_JUSTIFICATION);

            // Tipo 2 - operación aritmética mal resuelta

            case SUMA_RESUELTA_INCORRECTAMENTE:
                return createTextsFrom(context,
                        R.string.SUMA_RESUELTA_INCORRECTAMENTE_OPTION,
                        R.string.SUMA_RESUELTA_INCORRECTAMENTE_JUSTIFICATION);

            case RESTA_RESUELTA_INCORRECTAMENTE:
                return createTextsFrom(context,
                        R.string.RESTA_RESUELTA_INCORRECTAMENTE_OPTION,
                        R.string.RESTA_RESUELTA_INCORRECTAMENTE_JUSTIFICATION);

            case MULTIPLICACION_RESUELTA_INCORRECTAMENTE:
                return createTextsFrom(context,
                        R.string.MULTIPLICACION_RESUELTA_INCORRECTAMENTE_OPTION,
                        R.string.MULTIPLICACION_RESUELTA_INCORRECTAMENTE_JUSTIFICATION);

            case DIVISION_RESUELTA_INCORRECTAMENTE:
                return createTextsFrom(context,
                        R.string.DIVISION_RESUELTA_INCORRECTAMENTE_OPTION,
                        R.string.DIVISION_RESUELTA_INCORRECTAMENTE_JUSTIFICATION);

            case POTENCIA_RESUELTA_INCORRECTAMENTE:
                return createTextsFrom(context,
                        R.string.POTENCIA_RESUELTA_INCORRECTAMENTE_OPTION,
                        R.string.POTENCIA_RESUELTA_INCORRECTAMENTE_JUSTIFICATION);

            case RAIZ_RESUELTA_INCORRECTAMENTE:
                return createTextsFrom(context,
                        R.string.RAIZ_RESUELTA_INCORRECTAMENTE_OPTION,
                        R.string.RAIZ_RESUELTA_INCORRECTAMENTE_JUSTIFICATION);

            // Tipo 2 - distributiva mal hecha

            case DISTRIBUTIVA_BASICA_MAL_RESUELTA:
                return createTextsFrom(context,
                        R.string.DISTRIBUTIVA_BASICA_MAL_RESUELTA_OPTION,
                        R.string.DISTRIBUTIVA_BASICA_MAL_RESUELTA_JUSTIFICATION);

            case DISTRIBUTIVA_DOBLE_MAL_RESUELTA:
                return createTextsFrom(context,
                        R.string.DISTRIBUTIVA_DOBLE_MAL_RESUELTA_OPTION,
                        R.string.DISTRIBUTIVA_DOBLE_MAL_RESUELTA_JUSTIFICATION);

            // Tipo 2 - asociativa mal hecha

            case ASOCIATIVA_MAL_RESUELTA:
                return createTextsFrom(context,
                        R.string.ASOCIATIVA_MAL_RESUELTA_OPTION,
                        R.string.ASOCIATIVA_MAL_RESUELTA_JUSTIFICATION);

            // Tipo 2 - potencia de binomio mal resuelta

            case DISTRIBUTIVA_DE_POTENCIA_SOBRE_BINOMIO:
                return createTextsFrom(context,
                        R.string.DISTRIBUTIVA_DE_POTENCIA_SOBRE_BINOMIO_OPTION,
                        R.string.DISTRIBUTIVA_DE_POTENCIA_SOBRE_BINOMIO_JUSTIFICATION);

            case AGREGAR_PRODUCTO_A_MIEMBRO:
                return createTextsFrom(context,
                        R.string.AGREGAR_PRODUCTO_A_MIEMBRO_OPTION,
                        R.string.AGREGAR_PRODUCTO_A_MIEMBRO_JUSTIFICATION);
            case AGREGAR_SUMA_A_MIEMBRO:
                return createTextsFrom(context,
                        R.string.AGREGAR_SUMA_A_MIEMBRO_OPTION,
                        R.string.AGREGAR_SUMA_A_MIEMBRO_JUSTIFICATION);

            case CONSTANTE_NO_ENCONTRADA:
                break;
        }

        // TODO crear getIncorrectJustificationsFrom
        return createIncorrectTextsFrom(context,
                R.string.FACTOR_SUM_PRODUCT_RULE_OPTION,
                R.string.FACTOR_SUM_PRODUCT_RULE_JUSTIFICATION);
    }

    public static Map<String, String> getContextOfResolutionTexts(String contextOfResolution, Context context){
        if(SolvePolynomialActivity.CONTEXT_OF_RESOLUTION_IS_POLYNOMIAL_FACTORIZED.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.POLYNOMIAL_FACTORIZED_FIRST_TEXT, R.string.POLYNOMIAL_FACTORIZED_SECOND_TEXT, SolvePolynomialActivity.CONTEXT_OF_RESOLUTION_IS_POLYNOMIAL_FACTORIZED);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_DOMAIN.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.DOMAIN_FIRST_TEXT, R.string.DOMAIN_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_DOMAIN);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_IMAGE.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.IMAGE_FIRST_TEXT, R.string.IMAGE_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_IMAGE);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_ORIGIN_ORD.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.ORIGIN_ORD_FIRST_TEXT, R.string.ORIGIN_ORD_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_ORIGIN_ORD);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_ROOTS.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.ROOTS_FIRST_TEXT, R.string.ROOTS_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_ROOTS);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_FINITE_SOLUTIONS.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.EQUATION_WITH_FINITE_SOLUTIONS_FIRST_TEXT, R.string.EQUATION_WITH_FINITE_SOLUTIONS_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_FINITE_SOLUTIONS);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_INFINITE_SOLUTIONS.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.EQUATION_WITH_INFINITE_SOLUTIONS_FIRST_TEXT, R.string.EQUATION_WITH_INFINITE_SOLUTIONS_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_EQUATION_WITH_INFINITE_SOLUTIONS);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_EQUATION_WITHOUT_SOLUTIONS.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.EQUATION_WITHOUT_SOLUTIONS_FIRST_TEXT, R.string.EQUATION_WITHOUT_SOLUTIONS_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_EQUATION_WITHOUT_SOLUTIONS);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITH_INTERVAL_SOLUTIONS.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.INEQUATION_WITH_INTERVAL_SOLUTIONS_FIRST_TEXT, R.string.INEQUATION_WITH_INTERVAL_SOLUTIONS_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITH_INTERVAL_SOLUTIONS);
        }

        if(SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITHOUT_SOLUTIONS.equals(contextOfResolution)){
            return createContextOfResolutionTextsFrom(context, R.string.INEQUATION_WITHOUT_SOLUTIONS_FIRST_TEXT, R.string.INEQUATION_WITHOUT_SOLUTIONS_SECOND_TEXT, SolveEquationActivity.CONTEXT_OF_RESOLUTION_IS_INEQUATION_WITH_INTERVAL_SOLUTIONS);
        }

        return createContextOfResolutionTextsFrom(context, R.string.COMPLEX_SOLUTIONS_FIRST_TEXT, R.string.COMPLEX_SOLUTIONS_SECOND_TEXT, "complex_solutions");
    }

    private static Map<String, String> createIncorrectTextsFrom(Context context, int optionId, int justificationId){
        Map<String, String> justifications = new HashMap<>();
        justifications.put("option", context.getString(optionId));
        justifications.put("incorrectOptionJustification", context.getString(justificationId));
        return justifications;
    }

    private static Map<String, String> createContextOfResolutionTextsFrom(Context context, int firstTextId, int secondTextId, String type){
        Map<String, String> texts = new HashMap<>();
        texts.put("first", context.getString(firstTextId));
        texts.put("second", context.getString(secondTextId));
        texts.put("type", type);
        return texts;
    }

    public static Map<String, String> replacePatterns(Map<String, String> contextOfResolutionTexts, String key, String pattern, String newVal){
        String value = contextOfResolutionTexts.get(key);
        contextOfResolutionTexts.remove(key);
        contextOfResolutionTexts.put(key, ExpressionsManager.removeDecimals(value.replace(pattern, newVal)));
        return contextOfResolutionTexts;
    }
}
