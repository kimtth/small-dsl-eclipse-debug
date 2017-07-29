package org.xtext.labs.ui.contentassist;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.internal.text.html.HTMLPrinter;
import org.eclipse.jface.text.IRegion;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.ui.editor.hover.html.XtextBrowserInformationControlInput;
import org.eclipse.xtext.xbase.ui.hover.XbaseHoverProvider;
import org.xtext.labs.mydsl.StdFunction;

@SuppressWarnings("all")
public class MydslHoverProvider extends XbaseHoverProvider {
  /**
   * @comment:kim
   * this function will be activate when mouse cursor stay on the standard function.
   * small yellow pop-up with function description.
   */
  @Override
  public XtextBrowserInformationControlInput getHoverInfo(final EObject obj, final IRegion region, final XtextBrowserInformationControlInput prev) {
    if ((obj instanceof StdFunction)) {
      final String html = this.getHoverInfoAsHtml(obj);
      if ((html != null)) {
        final StringBuffer buffer = new StringBuffer(html);
        HTMLPrinter.insertPageProlog(buffer, 0, this.getStyleSheet());
        HTMLPrinter.addPageEpilog(buffer);
        String _string = buffer.toString();
        return new XtextBrowserInformationControlInput(prev, obj, _string, this.labelProvider);
      }
    }
    return super.getHoverInfo(obj, region, prev);
  }
  
  @Override
  public String getHoverInfoAsHtml(final EObject o) {
    String _xifexpression = null;
    if ((o instanceof StdFunction)) {
      String _xblockexpression = null;
      {
        final String funcname = ((StdFunction)o).getName();
        String rtn = "";
        String _switchResult = null;
        if (funcname != null) {
          switch (funcname) {
            case "printstr":
              String _rtn = rtn;
              StringConcatenation _builder = new StringConcatenation();
              _builder.append("<h3>printstr</h3>");
              _builder.newLine();
              _builder.append("\t\t\t\t\t");
              _builder.append("<ul>");
              _builder.newLine();
              _builder.append("\t\t\t\t\t\t");
              _builder.append("<li>definition: printstr(string)</li>");
              _builder.newLine();
              _builder.append("\t\t\t\t\t\t");
              _builder.append("<li>argumnets_1: string</li>");
              _builder.newLine();
              _builder.append("\t\t\t\t\t\t");
              _builder.append("<li>return: void</li>");
              _builder.newLine();
              _builder.append("\t\t\t\t\t\t");
              _builder.append("<li>description: print out string</li>");
              _builder.newLine();
              _builder.append("\t\t\t\t\t");
              _builder.append("</ul><br/>");
              _switchResult = rtn = (_rtn + _builder);
              break;
            case "strjoin":
              String _rtn_1 = rtn;
              StringConcatenation _builder_1 = new StringConcatenation();
              _builder_1.append("<h3>strjoin</h3>");
              _builder_1.newLine();
              _builder_1.append("\t\t\t\t\t");
              _builder_1.append("<ul>");
              _builder_1.newLine();
              _builder_1.append("\t\t\t\t\t\t");
              _builder_1.append("<li>definition: strjoin(string, string)</li>");
              _builder_1.newLine();
              _builder_1.append("\t\t\t\t\t\t");
              _builder_1.append("<li>argumnets_1: string</li>");
              _builder_1.newLine();
              _builder_1.append("\t\t\t\t\t\t");
              _builder_1.append("<li>argumnets_2: string</li>");
              _builder_1.newLine();
              _builder_1.append("\t\t\t\t\t\t");
              _builder_1.append("<li>return: string</li>");
              _builder_1.newLine();
              _builder_1.append("\t\t\t\t\t\t");
              _builder_1.append("<li>description: concatenate string</li>");
              _builder_1.newLine();
              _builder_1.append("\t\t\t\t\t");
              _builder_1.append("</ul><br/>");
              _switchResult = rtn = (_rtn_1 + _builder_1);
              break;
            case "strsplit":
              String _rtn_2 = rtn;
              StringConcatenation _builder_2 = new StringConcatenation();
              _builder_2.append("<h3>strsplit</h3>");
              _builder_2.newLine();
              _builder_2.append("\t\t\t\t\t");
              _builder_2.append("<ul>");
              _builder_2.newLine();
              _builder_2.append("\t\t\t\t\t\t");
              _builder_2.append("<li>definition: strsplit(string, delimeter)</li>");
              _builder_2.newLine();
              _builder_2.append("\t\t\t\t\t\t");
              _builder_2.append("<li>argumnets_1: string</li>");
              _builder_2.newLine();
              _builder_2.append("\t\t\t\t\t\t");
              _builder_2.append("<li>argumnets_2: string(delimeter)</li>");
              _builder_2.newLine();
              _builder_2.append("\t\t\t\t\t\t");
              _builder_2.append("<li>return: string[]</li>");
              _builder_2.newLine();
              _builder_2.append("\t\t\t\t\t\t");
              _builder_2.append("<li>description: split string by delimeter</li>");
              _builder_2.newLine();
              _builder_2.append("\t\t\t\t\t");
              _builder_2.append("</ul><br/>");
              _switchResult = rtn = (_rtn_2 + _builder_2);
              break;
            case "numtostr":
              String _rtn_3 = rtn;
              StringConcatenation _builder_3 = new StringConcatenation();
              _builder_3.append("<h3>numtostr</h3>");
              _builder_3.newLine();
              _builder_3.append("\t\t\t\t\t");
              _builder_3.append("<ul>");
              _builder_3.newLine();
              _builder_3.append("\t\t\t\t\t\t");
              _builder_3.append("<li>definition: numtostr(num)</li>");
              _builder_3.newLine();
              _builder_3.append("\t\t\t\t\t\t");
              _builder_3.append("<li>argumnets_1: num</li>");
              _builder_3.newLine();
              _builder_3.append("\t\t\t\t\t\t");
              _builder_3.append("<li>return: string</li>");
              _builder_3.newLine();
              _builder_3.append("\t\t\t\t\t\t");
              _builder_3.append("<li>description: convert num to string</li>");
              _builder_3.newLine();
              _builder_3.append("\t\t\t\t\t");
              _builder_3.append("</ul><br/>");
              _switchResult = rtn = (_rtn_3 + _builder_3);
              break;
          }
        }
        _xblockexpression = _switchResult;
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
}
