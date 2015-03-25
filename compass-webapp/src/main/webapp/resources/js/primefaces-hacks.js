/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */

/* @Hack: Primefaces doesn't support the 'greedy' attribute for the jQuery Droppable widgets it uses, so we have to
 * force jQuery to set them 'greedy' by default. This will stop drop events from bubbling up through the entire chain
 * of nested droppables (eg. in the scene hierarchy)
 * @Hack: Draggables should always be appended to the Body element so they will always appear on top of other elements
 * while dragging. */
$.extend($.ui.droppable.prototype.options, {greedy:true});
$.extend($.ui.draggable.prototype.options, {appendTo:"body"});
