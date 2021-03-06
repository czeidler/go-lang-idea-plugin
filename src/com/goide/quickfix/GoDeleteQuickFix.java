/*
 * Copyright 2013-2016 Sergey Ignatov, Alexander Zolotov, Florin Patan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.goide.quickfix;

import com.intellij.codeInspection.LocalQuickFixBase;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ObjectUtils;
import org.jetbrains.annotations.NotNull;

public class GoDeleteQuickFix extends LocalQuickFixBase {
  private final Class<? extends PsiElement> myClazz;
  private final IElementType myElementType;

  public GoDeleteQuickFix(@NotNull String name, @NotNull Class<? extends PsiElement> clazz) {
    super(name);
    myClazz = clazz;
    myElementType = null;
  }
  
  public GoDeleteQuickFix(@NotNull String name, @NotNull IElementType elementType) {
    super(name);
    myClazz = PsiElement.class;
    myElementType = elementType;
  }

  @Override
  public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor descriptor) {
    WriteCommandAction.runWriteCommandAction(project, () -> {
      PsiElement element = ObjectUtils.tryCast(descriptor.getStartElement(), myClazz);
      if (element != null && (myElementType == null || element.getNode().getElementType() == myElementType)) {
        element.delete();
      }
    });
  }
}