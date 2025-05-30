// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.codeInspection.dataFlow;

import com.intellij.codeInspection.CommonQuickFixBundle;
import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.dataFlow.jvm.SpecialField;
import com.intellij.codeInspection.dataFlow.types.DfType;
import com.intellij.codeInspection.dataFlow.types.DfTypes;
import com.intellij.codeInspection.util.OptionalUtil;
import com.intellij.modcommand.ModPsiUpdater;
import com.intellij.modcommand.PsiUpdateModCommandQuickFix;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.siyeh.ig.psiutils.ExpressionUtils;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author anet, peter
 */
public final class DfaOptionalSupport {

  @ApiStatus.Internal
  public static @Nullable LocalQuickFix registerReplaceOptionalOfWithOfNullableFix(@NotNull PsiExpression qualifier) {
    final PsiMethodCallExpression call = findCallExpression(qualifier);
    final PsiMethod method = call == null ? null : call.resolveMethod();
    final PsiClass containingClass = method == null ? null : method.getContainingClass();
    if (containingClass != null && "of".equals(method.getName())) {
      final String qualifiedName = containingClass.getQualifiedName();
      if (CommonClassNames.JAVA_UTIL_OPTIONAL.equals(qualifiedName)) {
        return new ReplaceOptionalCallFix("ofNullable", false);
      }
      if (OptionalUtil.GUAVA_OPTIONAL.equals(qualifiedName)) {
        return new ReplaceOptionalCallFix("fromNullable", false);
      }
    }
    return null;
  }

  private static PsiMethodCallExpression findCallExpression(@NotNull PsiElement anchor) {
    final PsiElement argList = PsiUtil.skipParenthesizedExprUp(anchor).getParent();
    if (argList instanceof PsiExpressionList) {
      final PsiElement parent = argList.getParent();
      if (parent instanceof PsiMethodCallExpression) {
        return (PsiMethodCallExpression)parent;
      }
    }
    return null;
  }

  public static @Nullable LocalQuickFix createReplaceOptionalOfNullableWithEmptyFix(@NotNull PsiElement anchor) {
    final PsiMethodCallExpression parent = findCallExpression(anchor);
    if (parent == null) return null;
    boolean jdkOptional = OptionalUtil.JDK_OPTIONAL_OF_NULLABLE.test(parent);
    return new ReplaceOptionalCallFix(jdkOptional ? "empty" : "absent", true);
  }

  public static @Nullable LocalQuickFix createReplaceOptionalOfNullableWithOfFix(@NotNull PsiElement anchor) {
    final PsiMethodCallExpression parent = findCallExpression(anchor);
    if (parent == null) return null;
    return new ReplaceOptionalCallFix("of", false);
  }

  /**
   * Creates a DfType which represents present or absent optional (non-null)
   * @param present whether the value should be present
   * @return a DfType representing an Optional
   */
  public static @NotNull DfType getOptionalValue(boolean present) {
    DfType valueType = present ? DfTypes.NOT_NULL_OBJECT : DfTypes.NULL;
    return SpecialField.OPTIONAL_VALUE.asDfType(valueType);
  }

  private static class ReplaceOptionalCallFix extends PsiUpdateModCommandQuickFix {
    private final String myTargetMethodName;
    private final boolean myClearArguments;

    ReplaceOptionalCallFix(final String targetMethodName, boolean clearArguments) {
      myTargetMethodName = targetMethodName;
      myClearArguments = clearArguments;
    }

    @Override
    public @NotNull String getFamilyName() {
      return CommonQuickFixBundle.message("fix.replace.with.x", "." + myTargetMethodName + "()");
    }

    @Override
    protected void applyFix(@NotNull Project project, @NotNull PsiElement element, @NotNull ModPsiUpdater updater) {
      final PsiMethodCallExpression methodCallExpression = PsiTreeUtil.getParentOfType(element, PsiMethodCallExpression.class);
      if (methodCallExpression != null) {
        ExpressionUtils.bindCallTo(methodCallExpression, myTargetMethodName);
        if (myClearArguments) {
          PsiExpressionList argList = methodCallExpression.getArgumentList();
          PsiExpression[] args = argList.getExpressions();
          if (args.length > 0) {
            argList.deleteChildRange(args[0], args[args.length - 1]);
          }
        }
      }
    }
  }
}
