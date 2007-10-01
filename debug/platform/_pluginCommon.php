<?php

	#*****************************************************************************
	#
	# Copyright (c) 2007 IBM Corporation and others.
 	# All rights reserved. This program and the accompanying materials
 	# are made available under the terms of the Eclipse Public License v1.0
 	# which accompanies this distribution, and is available at
 	# http://www.eclipse.org/legal/epl-v10.html
 	# Contributors:
	#     IBM Corporation - initial implementation
	#
	# Author: 		Debug Team
	# Date:			September 21, 2006
	#
	# Description: This is a contributed item that provides a common side bar for links etc.
	#
	#
	#****************************************************************************
    
    $commonplugin = <<<EOHTML
    <div class="sideitem">
		<h6>Build Notes</h6>
		<ul>
			<li><a href="/eclipse/debug/platform/build_notes/build_notes.php#uptotheminute">Up to The Minute</a></li>
	        <li><a href="/eclipse/debug/platform/build_notes/build_notes.php#previous">Previous Releases</a></li>
		</ul>
	</div>
	<div class="sideitem">
			<h6>Plugins</h6>
			<ul>
				<li><a href="http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.debug.core" target="_blank">debug.core</a></li>
        		<li><a href="http://dev.eclipse.org/viewcvs/index.cgi/org.eclipse.debug.ui" target="_blank">debug.ui</a></li>
			</ul>
		</div>
EOHTML
?>