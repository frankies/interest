<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
{{- if . }}
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>{{- escapeXML ( index . 0 ).Target }} - License Scan Report - {{ now }}</title>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/html2pdf.js/0.10.1/html2pdf.bundle.min.js"></script>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            padding: 20px;
            min-height: 100vh;
        }
        .container {
            max-width: 1400px;
            margin: 0 auto;
            background: white;
            border-radius: 16px;
            box-shadow: 0 20px 60px rgba(0,0,0,0.3);
            overflow: hidden;
        }
        .header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 40px;
            text-align: center;
        }
        .header h1 {
            font-size: 2.5em;
            margin-bottom: 10px;
            text-shadow: 2px 2px 4px rgba(0,0,0,0.2);
        }
        .header p { font-size: 1.1em; opacity: 0.9; }
        .meta-info {
            display: flex;
            justify-content: center;
            gap: 30px;
            margin-top: 20px;
            flex-wrap: wrap;
        }
        .meta-item {
            background: rgba(255,255,255,0.2);
            padding: 10px 20px;
            border-radius: 8px;
            backdrop-filter: blur(10px);
        }
        .content { padding: 40px; }
        .summary-cards {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 20px;
            margin-bottom: 40px;
        }
        .summary-card {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            transition: transform 0.3s ease;
        }
        .summary-card:hover { transform: translateY(-5px); }
        .summary-card h3 { font-size: 0.9em; opacity: 0.9; margin-bottom: 10px; }
        .summary-card .number { font-size: 2.5em; font-weight: bold; }
        .tabs {
            display: flex;
            border-bottom: 2px solid #e0e0e0;
            margin-bottom: 30px;
            gap: 5px;
            flex-wrap: wrap;
        }
        .tab-button {
            padding: 15px 30px;
            background: #f8f9fa;
            border: none;
            border-bottom: 3px solid transparent;
            cursor: pointer;
            font-size: 1.1em;
            font-weight: 600;
            color: #495057;
            transition: all 0.3s ease;
        }
        .tab-button:hover { background: #e9ecef; color: #212529; }
        .tab-button.active {
            background: white;
            color: #667eea;
            border-bottom-color: #667eea;
        }
        .tab-button::before { content: '📦'; margin-right: 8px; }
        .tab-content { display: none; animation: fadeIn 0.3s ease; }
        .tab-content.active { display: block; }
        @keyframes fadeIn {
            from { opacity: 0; transform: translateY(10px); }
            to { opacity: 1; transform: translateY(0); }
        }
        .filters {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 12px;
            margin-bottom: 30px;
            display: flex;
            gap: 20px;
            flex-wrap: wrap;
            align-items: center;
        }
        .filter-group {
            display: flex;
            flex-direction: column;
            gap: 8px;
            flex: 1;
            min-width: 200px;
        }
        .filter-group label { font-weight: 600; color: #495057; font-size: 0.9em; }
        .filter-group input {
            padding: 10px 15px;
            border: 2px solid #dee2e6;
            border-radius: 8px;
            font-size: 1em;
            transition: border-color 0.3s ease;
        }
        .filter-group input:focus {
            outline: none;
            border-color: #667eea;
        }
        .action-buttons {
            display: flex;
            gap: 10px;
            align-self: flex-end;
        }
        .reset-button, .pdf-button {
            padding: 10px 20px;
            color: white;
            border: none;
            border-radius: 8px;
            cursor: pointer;
            font-weight: 600;
            transition: background 0.3s ease;
        }
        .reset-button {
            background: #667eea;
        }
        .reset-button:hover { background: #5568d3; }
        .pdf-button {
            background: #28a745;
        }
        .pdf-button:hover { background: #218838; }
        .pdf-button::before { content: '📄 '; }
        .license-table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            border-radius: 12px;
            overflow: hidden;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .license-table th {
            background: #f8f9fa;
            padding: 15px;
            text-align: left;
            font-weight: 600;
            color: #495057;
            border-bottom: 2px solid #dee2e6;
        }
        .license-table td { padding: 12px 15px; border-bottom: 1px solid #e9ecef; }
        .license-table tr:hover { background: #f8f9fa; }
        .license-table tbody tr.hidden { display: none; }
        .license-badge, .severity-badge {
            display: inline-block;
            padding: 4px 12px;
            border-radius: 12px;
            font-size: 0.85em;
            font-weight: 600;
            text-transform: uppercase;
        }
        .clickable-badge {
            transition: transform 0.2s ease, box-shadow 0.2s ease;
            cursor: pointer;
        }
        .clickable-badge:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0,0,0,0.2);
        }
        .clickable-badge.active {
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.5);
            transform: scale(1.05);
        }
        .license-apache { background: #e3f2fd; color: #1976d2; }
        .license-mit { background: #f3e5f5; color: #7b1fa2; }
        .license-gpl { background: #fff3e0; color: #e65100; }
        .license-bsd { background: #e8f5e9; color: #2e7d32; }
        .license-unknown { background: #ffebee; color: #c62828; }
        .license-other { background: #e0e0e0; color: #424242; }
        .severity-critical { background: #ffebee; color: #c62828; }
        .severity-high { background: #fff3e0; color: #e65100; }
        .severity-medium { background: #fff9c4; color: #f57f17; }
        .severity-low { background: #e8f5e9; color: #2e7d32; }
        .severity-unknown { background: #e0e0e0; color: #424242; }
        .footer {
            text-align: center;
            padding: 30px;
            background: #f8f9fa;
            color: #6c757d;
            font-size: 0.9em;
        }
        .no-licenses {
            padding: 40px;
            text-align: center;
            color: #6c757d;
            font-style: italic;
        }
        .filter-status {
            background: #e7f3ff;
            border-left: 4px solid #667eea;
            padding: 12px 20px;
            margin-bottom: 20px;
            border-radius: 4px;
            display: none;
        }
        .filter-status.active {
            display: block;
        }
        .filter-status strong {
            color: #667eea;
        }
        .collapsible-section {
            margin-top: 20px;
        }
        .collapsible-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            cursor: pointer;
            padding: 10px 0;
            user-select: none;
        }
        .collapsible-header:hover {
            opacity: 0.8;
        }
        .collapse-icon {
            font-size: 1.2em;
            transition: transform 0.3s ease;
            margin-left: 10px;
        }
        .collapse-icon.expanded {
            transform: rotate(180deg);
        }
        .collapsible-content {
            max-height: 0;
            overflow: hidden;
            transition: max-height 0.3s ease;
        }
        .collapsible-content.expanded {
            max-height: 2000px;
        }
        @media print {
            body { background: white; padding: 0; }
            .filters, .tabs, .action-buttons { display: none; }
            .tab-content { display: block !important; }
            .collapsible-content { max-height: none !important; }
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🔍 License Scan Report</h1>
            <p>Dependency License Analysis Report</p>
            <div class="meta-info">
                <div class="meta-item"><strong>📅 Scan Time:</strong> {{ now }}</div>
                <div class="meta-item"><strong>🔧 Tool:</strong> Trivy</div>
                <div class="meta-item"><strong>📊 Target:</strong> {{- escapeXML ( index . 0 ).Target }}</div>
            </div>
        </div>
        <div class="content">
            {{- $totalLicenses := 0 }}
            {{- $licenseTypes := dict }}
            {{- range . }}
                {{- range .Licenses }}
                    {{- $totalLicenses = add $totalLicenses 1 }}
                    {{- $_ := set $licenseTypes .Name true }}
                {{- end }}
            {{- end }}
            <div class="summary-cards">
                <div class="summary-card">
                    <h3>Total Dependencies</h3>
                    <div class="number">{{ $totalLicenses }}</div>
                </div>
                <div class="summary-card" style="background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);">
                    <h3>License Types</h3>
                    <div class="number">{{ len $licenseTypes }}</div>
                </div>
                <div class="summary-card" style="background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);">
                    <h3>Scanned Targets</h3>
                    <div class="number">{{ len . }}</div>
                </div>
            </div>
            
            <div class="tabs">
            {{- range $idx, $result := . }}
                <button class="tab-button" onclick="switchTab('tab-{{ $idx }}')" {{- if eq $idx 0 }} id="first-tab"{{- end }}>{{ escapeXML .Target }}</button>
            {{- end }}
            </div>
            
            <div class="filters">
                <div class="filter-group">
                    <label for="nameFilter">Search by Dependency Name:</label>
                    <input type="text" id="nameFilter" placeholder="Enter dependency name..." oninput="applyFilters()">
                </div>
                <div class="action-buttons">
                    <button class="reset-button" onclick="resetFilters()">Reset Filters</button>
                    <button class="pdf-button" onclick="exportToPDF()">Export PDF</button>
                </div>
            </div>
            
            <div id="filterStatus" class="filter-status"></div>
            
            {{- range $idx, $result := . }}
            <div id="tab-{{ $idx }}" class="tab-content" {{- if eq $idx 0 }} style="display:block"{{- end }}>
                {{- if eq (len .Licenses) 0 }}
                <div class="no-licenses">No license information detected</div>
                {{- else }}
                {{- $severityStats := dict }}
                {{- $licenseStats := dict }}
                {{- range .Licenses }}
                    {{- $count := index $severityStats .Severity | default 0 }}
                    {{- $_ := set $severityStats .Severity (add $count 1) }}
                    {{- $count := index $licenseStats .Name | default 0 }}
                    {{- $_ := set $licenseStats .Name (add $count 1) }}
                {{- end }}
                
                <div style="padding: 20px; background: #f8f9fa; margin: 0 0 20px 0;">
                    <div style="margin-bottom: 15px;"><strong>Severity Statistics:</strong> <span style="font-size: 0.85em; color: #6c757d;">(Click to filter)</span></div>
                    <div style="display: flex; gap: 15px; flex-wrap: wrap; margin-bottom: 20px;">
                    {{- range $severity := list "CRITICAL" "HIGH" "UNKNOWN" "MEDIUM" "LOW" }}
                        {{- $count := index $severityStats $severity }}
                        {{- if $count }}
                        <span class="severity-badge severity-{{ $severity | lower }} clickable-badge" style="font-size: 1em; padding: 8px 16px;" onclick="filterBySeverity('{{ $severity }}')">{{ $severity }}: {{ $count }}</span>
                        {{- end }}
                    {{- end }}
                    </div>
                    
                    <div class="collapsible-section">
                        <div class="collapsible-header" onclick="toggleCollapse(this)">
                            <div><strong>License Type Statistics:</strong> <span style="font-size: 0.85em; color: #6c757d;">(Click to filter)</span></div>
                            <span class="collapse-icon">▼</span>
                        </div>
                        <div class="collapsible-content">
                            <div style="display: flex; gap: 15px; flex-wrap: wrap; padding-top: 10px;">
                            {{- range $license, $count := $licenseStats }}
                                {{- $licenseClass := "license-other" }}
                                {{- if contains "apache" ($license | lower) }}{{ $licenseClass = "license-apache" }}{{ end }}
                                {{- if contains "mit" ($license | lower) }}{{ $licenseClass = "license-mit" }}{{ end }}
                                {{- if or (contains "gpl" ($license | lower)) (contains "lgpl" ($license | lower)) }}{{ $licenseClass = "license-gpl" }}{{ end }}
                                {{- if contains "bsd" ($license | lower) }}{{ $licenseClass = "license-bsd" }}{{ end }}
                                {{- if or (eq $license "UNKNOWN") (eq $license "") }}{{ $licenseClass = "license-unknown" }}{{ end }}
                                <span class="license-badge {{ $licenseClass }} clickable-badge" style="font-size: 1em; padding: 8px 16px;" onclick="filterByLicense('{{ escapeXML $license }}')">{{ escapeXML $license }}: {{ $count }}</span>
                            {{- end }}
                            </div>
                        </div>
                    </div>
                </div>
                
                <table class="license-table">
                    <thead>
                        <tr>
                            <th style="width: 40%;">Dependency Name</th>
                            <th style="width: 30%;">License</th>
                            <th style="width: 30%;">Severity</th>
                        </tr>
                    </thead>
                    <tbody>
                    {{- range .Licenses }}
                        {{- $licenseClass := "license-other" }}
                        {{- if contains "apache" (.Name | lower) }}{{ $licenseClass = "license-apache" }}{{ end }}
                        {{- if contains "mit" (.Name | lower) }}{{ $licenseClass = "license-mit" }}{{ end }}
                        {{- if or (contains "gpl" (.Name | lower)) (contains "lgpl" (.Name | lower)) }}{{ $licenseClass = "license-gpl" }}{{ end }}
                        {{- if contains "bsd" (.Name | lower) }}{{ $licenseClass = "license-bsd" }}{{ end }}
                        {{- if or (eq .Name "UNKNOWN") (eq .Name "") }}{{ $licenseClass = "license-unknown" }}{{ end }}
                        
                        {{- $severityClass := "severity-unknown" }}
                        {{- if eq .Severity "CRITICAL" }}{{ $severityClass = "severity-critical" }}{{ end }}
                        {{- if eq .Severity "HIGH" }}{{ $severityClass = "severity-high" }}{{ end }}
                        {{- if eq .Severity "MEDIUM" }}{{ $severityClass = "severity-medium" }}{{ end }}
                        {{- if eq .Severity "LOW" }}{{ $severityClass = "severity-low" }}{{ end }}
                        
                        <tr data-severity="{{ escapeXML .Severity }}" data-license="{{ escapeXML .Name }}" data-name="{{ escapeXML .PkgName | lower }}">
                            <td><strong>{{ escapeXML .PkgName }}</strong></td>
                            <td><span class="license-badge {{ $licenseClass }}">{{ escapeXML .Name }}</span></td>
                            <td><span class="severity-badge {{ $severityClass }}">{{ escapeXML .Severity }}</span></td>
                        </tr>
                    {{- end }}
                    </tbody>
                </table>
                {{- end }}
            </div>
            {{- end }}
        </div>
        <div class="footer">
            <p>Generated by Trivy License Scanner | GitHub Actions CI/CD</p>
            <p>© 2026 - Automated License Compliance Report</p>
        </div>
    </div>
    <script>
        let currentSeverityFilter = '';
        let currentLicenseFilter = '';
        
        function switchTab(tabId) {
            document.querySelectorAll('.tab-content').forEach(c => c.style.display = 'none');
            document.querySelectorAll('.tab-button').forEach(b => b.classList.remove('active'));
            document.getElementById(tabId).style.display = 'block';
            event.target.classList.add('active');
            resetFilters();
        }
        
        function filterBySeverity(severity) {
            if (currentSeverityFilter === severity) {
                currentSeverityFilter = '';
            } else {
                currentSeverityFilter = severity;
            }
            currentLicenseFilter = '';
            updateBadgeStyles();
            applyFilters();
        }
        
        function filterByLicense(license) {
            if (currentLicenseFilter === license) {
                currentLicenseFilter = '';
            } else {
                currentLicenseFilter = license;
            }
            currentSeverityFilter = '';
            updateBadgeStyles();
            applyFilters();
        }
        
        function updateBadgeStyles() {
            document.querySelectorAll('.clickable-badge').forEach(badge => {
                badge.classList.remove('active');
            });
            
            if (currentSeverityFilter) {
                document.querySelectorAll('.clickable-badge').forEach(badge => {
                    if (badge.textContent.includes(currentSeverityFilter + ':')) {
                        badge.classList.add('active');
                    }
                });
            }
            if (currentLicenseFilter) {
                document.querySelectorAll('.clickable-badge').forEach(badge => {
                    if (badge.textContent.includes(currentLicenseFilter + ':')) {
                        badge.classList.add('active');
                    }
                });
            }
        }
        
        function applyFilters() {
            const nameFilter = document.getElementById('nameFilter').value.toLowerCase();
            const activeTab = document.querySelector('.tab-content[style*="display: block"]');
            if (!activeTab) return;
            
            const rows = activeTab.querySelectorAll('.license-table tbody tr');
            let visibleCount = 0;
            
            rows.forEach(row => {
                const severity = row.getAttribute('data-severity');
                const license = row.getAttribute('data-license');
                const name = row.getAttribute('data-name');
                
                const severityMatch = !currentSeverityFilter || severity === currentSeverityFilter;
                const licenseMatch = !currentLicenseFilter || license === currentLicenseFilter;
                const nameMatch = !nameFilter || name.includes(nameFilter);
                
                if (severityMatch && licenseMatch && nameMatch) {
                    row.classList.remove('hidden');
                    visibleCount++;
                } else {
                    row.classList.add('hidden');
                }
            });
            
            updateFilterStatus(visibleCount, rows.length);
        }
        
        function updateFilterStatus(visibleCount, totalCount) {
            const statusDiv = document.getElementById('filterStatus');
            if (currentSeverityFilter || currentLicenseFilter || document.getElementById('nameFilter').value) {
                let filterText = 'Current Filter: ';
                if (currentSeverityFilter) filterText += 'Severity=' + currentSeverityFilter + ' ';
                if (currentLicenseFilter) filterText += 'License=' + currentLicenseFilter + ' ';
                if (document.getElementById('nameFilter').value) filterText += 'Name="' + document.getElementById('nameFilter').value + '" ';
                filterText += '| Showing ' + visibleCount + ' / ' + totalCount + ' records';
                statusDiv.innerHTML = '<strong>🔍 ' + filterText + '</strong>';
                statusDiv.classList.add('active');
            } else {
                statusDiv.classList.remove('active');
            }
        }
        
        function resetFilters() {
            currentSeverityFilter = '';
            currentLicenseFilter = '';
            document.getElementById('nameFilter').value = '';
            updateBadgeStyles();
            applyFilters();
        }
        
        function toggleCollapse(header) {
            const content = header.nextElementSibling;
            const icon = header.querySelector('.collapse-icon');
            
            if (content.classList.contains('expanded')) {
                content.classList.remove('expanded');
                icon.classList.remove('expanded');
            } else {
                content.classList.add('expanded');
                icon.classList.add('expanded');
            }
        }
        
        function exportToPDF() {
            const element = document.querySelector('.container');
            const opt = {
                margin: 10,
                filename: 'license-scan-report.pdf',
                image: { type: 'jpeg', quality: 0.98 },
                html2canvas: { scale: 2, useCORS: true },
                jsPDF: { unit: 'mm', format: 'a4', orientation: 'portrait' }
            };
            
            const originalDisplay = [];
            document.querySelectorAll('.tab-content').forEach((content, index) => {
                originalDisplay[index] = content.style.display;
                content.style.display = 'block';
            });
            
            const filters = document.querySelector('.filters');
            const tabs = document.querySelector('.tabs');
            const filterStatus = document.getElementById('filterStatus');
            filters.style.display = 'none';
            tabs.style.display = 'none';
            filterStatus.style.display = 'none';
            
            document.querySelectorAll('.collapsible-content').forEach(content => {
                content.style.maxHeight = 'none';
            });
            
            html2pdf().set(opt).from(element).save().then(() => {
                document.querySelectorAll('.tab-content').forEach((content, index) => {
                    content.style.display = originalDisplay[index];
                });
                filters.style.display = 'flex';
                tabs.style.display = 'flex';
                if (filterStatus.classList.contains('active')) {
                    filterStatus.style.display = 'block';
                }
            });
        }
        
        document.addEventListener('DOMContentLoaded', function() {
            const firstTab = document.getElementById('first-tab');
            if (firstTab) {
                firstTab.classList.add('active');
            }
        });
    </script>
{{- else }}
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🔍 License Scan Report</h1>
            <p>Trivy Returned Empty Report</p>
        </div>
    </div>
{{- end }}
</body>
</html>
