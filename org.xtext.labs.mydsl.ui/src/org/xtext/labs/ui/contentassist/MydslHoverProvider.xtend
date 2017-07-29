package org.xtext.labs.ui.contentassist

import org.eclipse.emf.ecore.EObject
import org.eclipse.jface.internal.text.html.HTMLPrinter
import org.eclipse.jface.text.IRegion
import org.eclipse.xtext.ui.editor.hover.html.XtextBrowserInformationControlInput
import org.eclipse.xtext.xbase.ui.hover.XbaseHoverProvider
import org.xtext.labs.mydsl.StdFunction

class MydslHoverProvider extends XbaseHoverProvider {
	/**
	 * @comment:kim
	 * this function will be activate when mouse cursor stay on the standard function.
	 * small yellow pop-up with function description. 
	 */
	override XtextBrowserInformationControlInput getHoverInfo(EObject obj, IRegion region,
		XtextBrowserInformationControlInput prev) {
		if (obj instanceof StdFunction) {
			val html = getHoverInfoAsHtml(obj);
			if (html !== null) {
				val buffer = new StringBuffer(html);
				HTMLPrinter.insertPageProlog(buffer, 0, getStyleSheet());
				HTMLPrinter.addPageEpilog(buffer);
				return new XtextBrowserInformationControlInput(prev, obj, buffer.toString(), labelProvider);
			}
		}
		return super.getHoverInfo(obj, region, prev);
	}

	override String getHoverInfoAsHtml(EObject o) {
		if (o instanceof StdFunction) {
			val funcname = o.name
			var rtn = ''

			switch funcname {
				case 'printstr':
					rtn += '''<h3>printstr</h3>
					<ul>
						<li>definition: printstr(string)</li>
						<li>argumnets_1: string</li>
						<li>return: void</li>
						<li>description: print out string</li>
					</ul><br/>'''
				case 'strjoin':
					rtn += '''<h3>strjoin</h3>
					<ul>
						<li>definition: strjoin(string, string)</li>
						<li>argumnets_1: string</li>
						<li>argumnets_2: string</li>
						<li>return: string</li>
						<li>description: concatenate string</li>
					</ul><br/>'''
				case 'strsplit':
					rtn += '''<h3>strsplit</h3>
					<ul>
						<li>definition: strsplit(string, delimeter)</li>
						<li>argumnets_1: string</li>
						<li>argumnets_2: string(delimeter)</li>
						<li>return: string[]</li>
						<li>description: split string by delimeter</li>
					</ul><br/>'''
				case 'numtostr':
					rtn += '''<h3>numtostr</h3>
					<ul>
						<li>definition: numtostr(num)</li>
						<li>argumnets_1: num</li>
						<li>return: string</li>
						<li>description: convert num to string</li>
					</ul><br/>'''
			}
		}
	}
}
