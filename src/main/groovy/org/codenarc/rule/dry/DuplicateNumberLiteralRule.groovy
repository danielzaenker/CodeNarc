/*
 * Copyright 2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.dry

import org.codenarc.rule.AbstractAstVisitorRule
import org.codenarc.rule.AstVisitor

/**
 * Checks for duplication of constant number literal values.
 * <p/>
 * Set the optional <code>ignoreNumbers</code> property to a comma-separated list (String) of
 * the numbers that should be ignored by this rule (i.e., not cause a violation). This property
 * defaults to "0,1" to ignore the constants zero and one.
 *
 * @author Chris Mair
 * @version $Revision: 428 $ - $Date: 2010-11-06 16:36:05 -0400 (Sat, 06 Nov 2010) $
 */
class DuplicateNumberLiteralRule extends AbstractAstVisitorRule {
    String name = 'DuplicateNumberLiteral'
    int priority = 2
    String doNotApplyToFilesMatching = DEFAULT_TEST_FILES
    String ignoreNumbers = '0,1'

    @Override
    AstVisitor getAstVisitor() {
        def ignoreValuesSet = parseIgnoreValues()
        new DuplicateLiteralAstVisitor(Number, ignoreValuesSet)
    }

    private Set parseIgnoreValues() {
        def strings = ignoreNumbers ? ignoreNumbers.tokenize(',') : []
        def numbers = strings.collect { str -> parseNumber(str)  }
        numbers as Set
    }

    private parseNumber(String string) {
        if (string.isInteger()) {
            return string.toInteger()
        }
        else if (string.isBigDecimal()) {
            return string.toBigDecimal()
        }
        else {
            throw new NumberFormatException("[$string] is not a valid number")
        }
    }
}