package org.xtext.labs.ui.minimap;

import java.lang.reflect.Method;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BidiSegmentEvent;
import org.eclipse.swt.custom.BidiSegmentListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;

/**
 * @comment: kim
 * this src from https://github.com/apauzies/eclipse-minimap-view
 * thanks for contribution
 * */
public class MiniMapView extends ViewPart {

	public static final String ID = "minimap.MiniMapView";

	private Color backgroundColor;
	private Color foregroundColor;
	private Color selectionBackgroundColor;
	private Color selectionForegroundColor;

	private Font font = new Font(Display.getCurrent(), "Arial", 2, SWT.NONE);
	private AbstractTextEditor selectedPart;
	private MyTextEditor editor;
	private boolean mouseIsDown;
	private StyledText minimap;

	class MyTextEditor {
		private final AbstractTextEditor editor;

		public MyTextEditor(AbstractTextEditor editor) {
			this.editor = editor;

		}

		public ISourceViewer getSourceViewer() {
			try {
				Method m = AbstractTextEditor.class.getDeclaredMethod("getSourceViewer");
				m.setAccessible(true);

				return (ISourceViewer) m.invoke(editor);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}
	}

	private static void fillMiniMap(StyledText minimap, MyTextEditor editor) {
		StyledText text = editor.getSourceViewer().getTextWidget();

		// text.setAlwaysShowScrollBars(true); //for Scroll bars
		minimap.setContent(text.getContent());
		minimap.setStyleRanges(editor.getSourceViewer().getTextWidget().getStyleRanges());
		minimap.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).span(2, 1).create());
	}

	private void highlightVisibleRegion(StyledText minimap, MyTextEditor editor) {
		int start = editor.getSourceViewer().getTopIndexStartOffset();
		int end = editor.getSourceViewer().getBottomIndexEndOffset();

		// fix by kim
		// Caused by: java.lang.IllegalArgumentException: Index out of bounds
		int startLine = 0;
		try {
			startLine = minimap.getLineAtOffset(start);
		} catch (Exception e) {
			return;
		}

		int endLine = 0;
		try {
			endLine = minimap.getLineAtOffset(end);
		} catch (Exception e) {
			return;
		}

		// Reset
		try {
			minimap.setLineBackground(0, minimap.getLineCount() - 1, backgroundColor);
			minimap.setLineBackground(startLine, endLine - startLine + 1, selectionBackgroundColor);
		} catch (Exception e) {
			return;
		}
		// fix by kim

		// minimap.setSelection(minimap.getOffsetAtLine(startLine),
		// minimap.getOffsetAtLine(endLine - startLine +1));
	}

	private void changeEditor(MouseEvent e) {
		StyledText text = (StyledText) e.getSource();
		// text.setAlwaysShowScrollBars(true); //for Scroll bars
		int line = text.getLineAtOffset(text.getCaretOffset());
		// editor.getSourceViewer().getTextWidget().setCaretOffset(text.getCaretOffset());
		editor.getSourceViewer().getTextWidget().setTopIndex(line);
	}

	private final MouseListener minimapMouseListener = new MouseAdapter() {
		@Override
		public void mouseDown(MouseEvent e) {
			changeEditor(e);
			mouseIsDown = true;
		}

		@Override
		public void mouseUp(MouseEvent e) {
			changeEditor(e);
			mouseIsDown = false;
		}
	};

	private final MouseMoveListener minimapMouseMoveListener = new MouseMoveListener() {
		@Override
		public void mouseMove(MouseEvent e) {
			if (mouseIsDown) {
				changeEditor(e);
			}
		}
	};

	private final MouseWheelListener minimapMouseWheelListener = new MouseWheelListener() {
		@Override
		public void mouseScrolled(MouseEvent e) {
			editor.getSourceViewer().getTextWidget()
					.setTopIndex(editor.getSourceViewer().getTextWidget().getTopIndex() + (e.count * -1));
		}
	};

	private final SelectionListener minimapSelectionListener = new SelectionAdapter() {
		@Override
		public void widgetSelected(SelectionEvent event) {
			int offset = minimap.getCaretOffset();
			if (offset == event.x) {
				// right to left select
				minimap.setSelection(event.x, event.x);
			} else {
				// left to right select
				minimap.setSelection(event.y, event.y);
			}
		}
	};

	private final ControlListener editorControlListener = new ControlListener() {

		@Override
		public void controlResized(ControlEvent e) {

			if (minimap.getEnabled() && editor != null) {
				highlightVisibleRegion(minimap, editor);
			}
		}

		@Override
		public void controlMoved(ControlEvent e) {
		}
	};

	private final ISelectionListener editorSelectionListener = new ISelectionListener() {

		@Override
		public void selectionChanged(IWorkbenchPart part, ISelection selection) {
			if (part == selectedPart) {
				return;
			}
			if (!(part instanceof AbstractTextEditor)) {
				return;
			}

			// fix by kim
			if (editor != null) {
				if (editor.getSourceViewer() != null)
					editor.getSourceViewer().getTextWidget().removeControlListener(editorControlListener);
				if (editor.getSourceViewer() != null)
					editor.getSourceViewer().getTextWidget().removeBidiSegmentListener(editorSegmentListener);
			}

			selectedPart = (AbstractTextEditor) part;
			editor = new MyTextEditor((AbstractTextEditor) part);

			fillMiniMap(minimap, editor);
			highlightVisibleRegion(minimap, editor);

			editor.getSourceViewer().getTextWidget().addControlListener(editorControlListener);
			editor.getSourceViewer().getTextWidget().addBidiSegmentListener(editorSegmentListener);

			foregroundColor = editor.getSourceViewer().getTextWidget().getForeground();
			backgroundColor = editor.getSourceViewer().getTextWidget().getBackground();
			selectionBackgroundColor = editor.getSourceViewer().getTextWidget().getSelectionBackground();
			selectionForegroundColor = editor.getSourceViewer().getTextWidget().getSelectionForeground();

			minimap.setBackground(backgroundColor);
			minimap.setForeground(foregroundColor);
			minimap.setSelectionBackground(selectionBackgroundColor);
			minimap.setSelectionForeground(selectionForegroundColor);
		}
	};

	private final BidiSegmentListener editorSegmentListener = new BidiSegmentListener() {

		@Override
		public void lineGetSegments(BidiSegmentEvent event) {
			highlightVisibleRegion(minimap, editor);
		}
	};

	@Override
	public void createPartControl(Composite parent) {

		minimap = new StyledText(parent, SWT.V_SCROLL); // for scrolling
														// SWT.V_SCROLL
		minimap.setEditable(false);
		// minimap.setEnabled(true);
		minimap.setFont(font);
		// minimap.setCursor(null);
		minimap.setCaret(null);

		minimap.addMouseListener(minimapMouseListener);
		minimap.addMouseMoveListener(minimapMouseMoveListener);
		minimap.addMouseWheelListener(minimapMouseWheelListener);
		// Disable Selection
		minimap.addSelectionListener(minimapSelectionListener);

		getSite().getWorkbenchWindow().getSelectionService().addSelectionListener(editorSelectionListener);
	}

	@Override
	public void setFocus() {

	}

	@Override
	public void dispose() {
		super.dispose();
		try {
			editor.getSourceViewer().getTextWidget().removeControlListener(editorControlListener);
			editor.getSourceViewer().getTextWidget().removeBidiSegmentListener(editorSegmentListener);
			getSite().getWorkbenchWindow().getSelectionService().removeSelectionListener(editorSelectionListener);
		} catch (Exception e) {
			System.out.println("Minimap Dispose Error");
		}
	}

}
