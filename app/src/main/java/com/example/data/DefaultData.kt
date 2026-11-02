package com.example.data

import com.example.data.model.*

object DefaultData {

    val subjects = listOf(
        SubjectEntity(
            id = "FA",
            code = "FA",
            name = "Financial Accounting",
            colorHex = 0xFF0284C7, // Electric Blue
            isEditable = false,
            totalUnitsCount = 5,
            description = "Core accounting processes, financial statements & computerised systems"
        ),
        SubjectEntity(
            id = "BL",
            code = "BL",
            name = "Business Laws",
            colorHex = 0xFF6366F1, // Indigo
            isEditable = false,
            totalUnitsCount = 5,
            description = "Contract acts, Sale of Goods, and Limited Liability Partnership"
        ),
        SubjectEntity(
            id = "MPA",
            code = "MPA",
            name = "Management Principles & Applications",
            colorHex = 0xFF0EA5E9, // Sky Blue
            isEditable = false,
            totalUnitsCount = 5,
            description = "Planning, organising, directing, controlling and modern management"
        ),
        SubjectEntity(
            id = "GE",
            code = "GE",
            name = "Generic Elective (GE)",
            colorHex = 0xFF10B981, // Emerald Green
            isEditable = true,
            totalUnitsCount = 5,
            description = "Editable elective course syllabus with 5 customizable topic slots"
        ),
        SubjectEntity(
            id = "AEC",
            code = "AEC",
            name = "Ability Enhancement Course (AEC)",
            colorHex = 0xFFF59E0B, // Amber
            isEditable = true,
            totalUnitsCount = 5,
            description = "Editable communication and environmental ability skills"
        ),
        SubjectEntity(
            id = "SEC",
            code = "SEC",
            name = "Skill Enhancement Course (SEC)",
            colorHex = 0xFFEC4899, // Pink
            isEditable = true,
            totalUnitsCount = 5,
            description = "Editable practical computer, data & workplace skill modules"
        ),
        SubjectEntity(
            id = "VAC",
            code = "VAC",
            name = "Value Addition Course (VAC)",
            colorHex = 0xFF8B5CF6, // Purple
            isEditable = true,
            totalUnitsCount = 5,
            description = "Editable ethics, constitutional values & social engagement"
        )
    )

    val units = listOf(
        // FINANCIAL ACCOUNTING (5 Units)
        StudyUnitEntity(
            id = "FA_U1",
            subjectId = "FA",
            unitNumber = 1,
            title = "Theoretical Framework of Accounting and Accounting Process",
            importantTopics = "Accounting principles, GAAP, accounting standards, journal, ledger, trial balance, depreciation methods",
            personalNotes = "Master double entry mechanics, golden rules, capital vs revenue expenditures.",
            startDate = "2026-11-03",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "FA_U2",
            subjectId = "FA",
            unitNumber = 2,
            title = "Business Income and Accounting for Property, Plant & Equipment; Valuation of Inventory",
            importantTopics = "AS-10 (PPE), AS-2 (Inventory valuation FIFO vs Weighted Avg), measurement of business income",
            personalNotes = "Practice numerical questions on PPE revaluation, impairment, and inventory NRV.",
            startDate = "2026-11-06",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "FA_U3",
            subjectId = "FA",
            unitNumber = 3,
            title = "Financial Statements of Sole Proprietorship and Not-for-Profit Organisations",
            importantTopics = "Trading & P/L account, Balance Sheet adjustments, Receipts & Payments, Income & Expenditure account",
            personalNotes = "Pay special attention to adjustment entries: prepaid/accrued, subscription adjustments.",
            startDate = "2026-11-10",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "FA_U4",
            subjectId = "FA",
            unitNumber = 4,
            title = "Accounting for Inland Branches, Departments and Leases",
            importantTopics = "Debtors system, Stock and Debtors system, Departmental allocation, AS-19 Finance vs Operating leases",
            personalNotes = "Branch accounting ledger entries and loading reconciliations are crucial.",
            startDate = "2026-11-13",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "FA_U5",
            subjectId = "FA",
            unitNumber = 5,
            title = "Computerised Accounting System",
            importantTopics = "CAS fundamentals, features of accounting packages (Tally/ERP), database design, advantages vs limitations",
            personalNotes = "Review short theoretical notes, audit trails, and security controls.",
            startDate = "2026-11-17",
            deadlineDate = "2026-11-24"
        ),

        // BUSINESS LAWS (5 Units)
        StudyUnitEntity(
            id = "BL_U1",
            subjectId = "BL",
            unitNumber = 1,
            title = "The Indian Contract Act, 1872: Nature and Formation",
            importantTopics = "Offer & acceptance, consideration (privity rule), capacity to contract, free consent (coercion, fraud, misrepresentation)",
            personalNotes = "Memorize landmark cases: Balfour v Balfour, Carlill v Carbolic Smoke Ball, Mohori Bibee.",
            startDate = "2026-11-03",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "BL_U2",
            subjectId = "BL",
            unitNumber = 2,
            title = "Discharge of Contract and Remedies for Breach of Contract",
            importantTopics = "Modes of discharge (performance, agreement, frustration Sec 56), Hadley v Baxendale damages rules",
            personalNotes = "Distinguish liquidated damages vs penalty; doctrine of supervening impossibility.",
            startDate = "2026-11-07",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "BL_U3",
            subjectId = "BL",
            unitNumber = 3,
            title = "Introduction to Special Contracts",
            importantTopics = "Indemnity vs Guarantee, Bailment & Pledge rights and duties, Agency creation & termination",
            personalNotes = "Rights of surety, discharge of surety by creditor actions, finder of goods.",
            startDate = "2026-11-10",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "BL_U4",
            subjectId = "BL",
            unitNumber = 4,
            title = "The Sale of Goods Act, 1930",
            importantTopics = "Sale vs Agreement to Sell, Conditions and Warranties (Caveat Emptor & exceptions), Unpaid Seller rights",
            personalNotes = "Right of lien, stoppage in transit, transfer of title (Nemo dat quod non habet exceptions).",
            startDate = "2026-11-14",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "BL_U5",
            subjectId = "BL",
            unitNumber = 5,
            title = "The Limited Liability Partnership Act, 2008",
            importantTopics = "Salient features of LLP, LLP vs Partnership vs Company, Designated partners, Incorporation process, Winding up",
            personalNotes = "Small LLP definitions, statutory compliances and liability of partners.",
            startDate = "2026-11-17",
            deadlineDate = "2026-11-24"
        ),

        // MANAGEMENT PRINCIPLES & APPLICATIONS (5 Units)
        StudyUnitEntity(
            id = "MPA_U1",
            subjectId = "MPA",
            unitNumber = 1,
            title = "Introduction to Management",
            importantTopics = "Concept, significance, functions of management, Fayol's 14 principles, Taylor's Scientific management, Hawthorne studies",
            personalNotes = "Contrast Classical, Neo-classical and Contingency perspectives.",
            startDate = "2026-11-04",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "MPA_U2",
            subjectId = "MPA",
            unitNumber = 2,
            title = "Planning and Strategic Management",
            importantTopics = "Planning process, types of plans, MBO (Management by Objectives), SWOT analysis, Decision making models",
            personalNotes = "Understand bounded rationality, decision making under risk & uncertainty.",
            startDate = "2026-11-07",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "MPA_U3",
            subjectId = "MPA",
            unitNumber = 3,
            title = "Organising",
            importantTopics = "Formal vs informal organisation, span of management, centralization vs decentralization, delegation, matrix structure",
            personalNotes = "Differences between tall vs flat organizational designs.",
            startDate = "2026-11-11",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "MPA_U4",
            subjectId = "MPA",
            unitNumber = 4,
            title = "Directing and Controlling",
            importantTopics = "Motivation theories (Maslow, Herzberg, McGregor), Leadership styles, Control process, Feedforward/Feedback",
            personalNotes = "Control techniques: Budgetary control, PERT/CPM, balanced scorecard.",
            startDate = "2026-11-14",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "MPA_U5",
            subjectId = "MPA",
            unitNumber = 5,
            title = "Salient Developments and Contemporary Issues in Management",
            importantTopics = "CSR, Corporate governance, Diversity management, Total Quality Management (TQM), Change management",
            personalNotes = "Modern agile management, ethics and environmental sustainability.",
            startDate = "2026-11-18",
            deadlineDate = "2026-11-24"
        ),

        // GENERIC ELECTIVE (GE) (5 Units - Editable placeholders)
        StudyUnitEntity(
            id = "GE_U1",
            subjectId = "GE",
            unitNumber = 1,
            title = "GE Topic 1: Foundations & Core Principles",
            importantTopics = "Fundamental definitions, core concepts, theoretical paradigms",
            personalNotes = "Customizable for your specific chosen GE paper.",
            startDate = "2026-11-04",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "GE_U2",
            subjectId = "GE",
            unitNumber = 2,
            title = "GE Topic 2: Analytical Frameworks & Models",
            importantTopics = "Key frameworks, structural components, methodologies",
            personalNotes = "Add unit title and lecture notes here.",
            startDate = "2026-11-08",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "GE_U3",
            subjectId = "GE",
            unitNumber = 3,
            title = "GE Topic 3: Empirical Applications & Data",
            importantTopics = "Case studies, practical applications, empirical findings",
            personalNotes = "Review sample problems and past papers.",
            startDate = "2026-11-11",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "GE_U4",
            subjectId = "GE",
            unitNumber = 4,
            title = "GE Topic 4: Policy Analysis & Dynamics",
            importantTopics = "Contemporary trends, policy mechanisms, comparative review",
            personalNotes = "Keep key definitions handy for short answers.",
            startDate = "2026-11-15",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "GE_U5",
            subjectId = "GE",
            unitNumber = 5,
            title = "GE Topic 5: Advanced Readings & Review",
            importantTopics = "Synthesis, critical appraisal, revision questions",
            personalNotes = "Final compilation for examination.",
            startDate = "2026-11-18",
            deadlineDate = "2026-11-24"
        ),

        // ABILITY ENHANCEMENT COURSE (AEC) (5 Units - Editable)
        StudyUnitEntity(
            id = "AEC_U1",
            subjectId = "AEC",
            unitNumber = 1,
            title = "AEC Topic 1: Core Concepts & Literacy",
            importantTopics = "Basic definitions, terminology, core foundations",
            personalNotes = "Fully editable syllabus unit.",
            startDate = "2026-11-05",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "AEC_U2",
            subjectId = "AEC",
            unitNumber = 2,
            title = "AEC Topic 2: Applied Techniques & Frameworks",
            importantTopics = "Process mechanics, critical evaluation, strategies",
            personalNotes = "Revise key rules and principles.",
            startDate = "2026-11-08",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "AEC_U3",
            subjectId = "AEC",
            unitNumber = 3,
            title = "AEC Topic 3: Analytical Writing & Discourse",
            importantTopics = "Structure, drafting, argument synthesis",
            personalNotes = "Practice formats and past exam questions.",
            startDate = "2026-11-12",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "AEC_U4",
            subjectId = "AEC",
            unitNumber = 4,
            title = "AEC Topic 4: Contemporary Themes & Policy",
            importantTopics = "Modern issues, case studies, societal impact",
            personalNotes = "Highlight salient points.",
            startDate = "2026-11-15",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "AEC_U5",
            subjectId = "AEC",
            unitNumber = 5,
            title = "AEC Topic 5: Comprehensive Review & Practice",
            importantTopics = "Summary notes, previous year questions, mock test",
            personalNotes = "Ready for revision rounds.",
            startDate = "2026-11-19",
            deadlineDate = "2026-11-24"
        ),

        // SKILL ENHANCEMENT COURSE (SEC) (5 Units - Editable)
        StudyUnitEntity(
            id = "SEC_U1",
            subjectId = "SEC",
            unitNumber = 1,
            title = "SEC Topic 1: Practical Fundamentals",
            importantTopics = "Tools, terminology, hands-on workspace basics",
            personalNotes = "Customizable unit for your SEC course.",
            startDate = "2026-11-05",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "SEC_U2",
            subjectId = "SEC",
            unitNumber = 2,
            title = "SEC Topic 2: Applied Operations & Methods",
            importantTopics = "Workflow patterns, operational steps, troubleshooting",
            personalNotes = "Record lab exercises and steps.",
            startDate = "2026-11-09",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "SEC_U3",
            subjectId = "SEC",
            unitNumber = 3,
            title = "SEC Topic 3: Data Handling & Problem Solving",
            importantTopics = "Formulas, analysis, practical demonstrations",
            personalNotes = "Execute practical assignments.",
            startDate = "2026-11-12",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "SEC_U4",
            subjectId = "SEC",
            unitNumber = 4,
            title = "SEC Topic 4: Professional Output & Reporting",
            importantTopics = "Presentation, formatting, portfolio documentation",
            personalNotes = "Refine final presentation.",
            startDate = "2026-11-16",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "SEC_U5",
            subjectId = "SEC",
            unitNumber = 5,
            title = "SEC Topic 5: Practical Mastery & Evaluation",
            importantTopics = "Comprehensive review, mock practical, viva prep",
            personalNotes = "Final checklist completed.",
            startDate = "2026-11-19",
            deadlineDate = "2026-11-24"
        ),

        // VALUE ADDITION COURSE (VAC) (5 Units - Editable)
        StudyUnitEntity(
            id = "VAC_U1",
            subjectId = "VAC",
            unitNumber = 1,
            title = "VAC Topic 1: Philosophical & Ethical Foundations",
            importantTopics = "Key values, moral traditions, social ethics",
            personalNotes = "Customizable for your selected VAC topic.",
            startDate = "2026-11-06",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "VAC_U2",
            subjectId = "VAC",
            unitNumber = 2,
            title = "VAC Topic 2: Values in Practice & Citizenship",
            importantTopics = "Civic duties, community engagement, equality",
            personalNotes = "Review primary readings and quotes.",
            startDate = "2026-11-09",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "VAC_U3",
            subjectId = "VAC",
            unitNumber = 3,
            title = "VAC Topic 3: Sustainability & Global Perspectives",
            importantTopics = "Environmental stewardship, sustainable living, empathy",
            personalNotes = "Summarize short notes.",
            startDate = "2026-11-13",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "VAC_U4",
            subjectId = "VAC",
            unitNumber = 4,
            title = "VAC Topic 4: Conflict Resolution & Harmony",
            importantTopics = "Non-violence, dialogue, social inclusion",
            personalNotes = "Case study interpretations.",
            startDate = "2026-11-16",
            deadlineDate = "2026-11-24"
        ),
        StudyUnitEntity(
            id = "VAC_U5",
            subjectId = "VAC",
            unitNumber = 5,
            title = "VAC Topic 5: Integration & Personal Reflection",
            importantTopics = "Reflective essays, value assimilation, exam prep",
            personalNotes = "Review key themes.",
            startDate = "2026-11-20",
            deadlineDate = "2026-11-24"
        )
    )

    val scheduleTasks = listOf(
        // === PHASE 1: SYLLABUS COMPLETION (3 Nov – 24 Nov) ===
        // 3 Nov
        ScheduleTaskEntity("TASK_20261103_1", "2026-11-03", "4:00 PM – 6:00 PM", "FA", "Financial Accounting", "FA_U1", "Financial Accounting Unit 1", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261103_2", "2026-11-03", "6:30 PM – 8:30 PM", "BL", "Business Laws", "BL_U1", "Business Laws Unit 1", SchedulePhase.SYLLABUS_COMPLETION),

        // 4 Nov
        ScheduleTaskEntity("TASK_20261104_1", "2026-11-04", "4:00 PM – 6:00 PM", "MPA", "Management Principles & Applications", "MPA_U1", "MPA Unit 1", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261104_2", "2026-11-04", "6:30 PM – 8:30 PM", "GE", "Generic Elective (GE)", "GE_U1", "GE Topic 1", SchedulePhase.SYLLABUS_COMPLETION),

        // 5 Nov
        ScheduleTaskEntity("TASK_20261105_1", "2026-11-05", "4:00 PM – 6:00 PM", "AEC", "Ability Enhancement Course", "AEC_U1", "AEC Topic 1", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261105_2", "2026-11-05", "6:30 PM – 8:30 PM", "SEC", "Skill Enhancement Course", "SEC_U1", "SEC Topic 1", SchedulePhase.SYLLABUS_COMPLETION),

        // 6 Nov
        ScheduleTaskEntity("TASK_20261106_1", "2026-11-06", "4:00 PM – 6:00 PM", "VAC", "Value Addition Course", "VAC_U1", "VAC Topic 1", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261106_2", "2026-11-06", "6:30 PM – 8:30 PM", "FA", "Financial Accounting", "FA_U2", "Financial Accounting Unit 2", SchedulePhase.SYLLABUS_COMPLETION),

        // 7 Nov
        ScheduleTaskEntity("TASK_20261107_1", "2026-11-07", "4:00 PM – 6:00 PM", "BL", "Business Laws", "BL_U2", "Business Laws Unit 2", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261107_2", "2026-11-07", "6:30 PM – 8:30 PM", "MPA", "Management Principles & Applications", "MPA_U2", "MPA Unit 2", SchedulePhase.SYLLABUS_COMPLETION),

        // 8 Nov
        ScheduleTaskEntity("TASK_20261108_1", "2026-11-08", "4:00 PM – 6:00 PM", "GE", "Generic Elective (GE)", "GE_U2", "GE Topic 2", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261108_2", "2026-11-08", "6:30 PM – 8:30 PM", "AEC", "Ability Enhancement Course", "AEC_U2", "AEC Topic 2", SchedulePhase.SYLLABUS_COMPLETION),

        // 9 Nov
        ScheduleTaskEntity("TASK_20261109_1", "2026-11-09", "4:00 PM – 6:00 PM", "SEC", "Skill Enhancement Course", "SEC_U2", "SEC Topic 2", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261109_2", "2026-11-09", "6:30 PM – 8:30 PM", "VAC", "Value Addition Course", "VAC_U2", "VAC Topic 2", SchedulePhase.SYLLABUS_COMPLETION),

        // 10 Nov
        ScheduleTaskEntity("TASK_20261110_1", "2026-11-10", "4:00 PM – 6:00 PM", "FA", "Financial Accounting", "FA_U3", "Financial Accounting Unit 3", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261110_2", "2026-11-10", "6:30 PM – 8:30 PM", "BL", "Business Laws", "BL_U3", "Business Laws Unit 3", SchedulePhase.SYLLABUS_COMPLETION),

        // 11 Nov
        ScheduleTaskEntity("TASK_20261111_1", "2026-11-11", "4:00 PM – 6:00 PM", "MPA", "Management Principles & Applications", "MPA_U3", "MPA Unit 3", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261111_2", "2026-11-11", "6:30 PM – 8:30 PM", "GE", "Generic Elective (GE)", "GE_U3", "GE Topic 3", SchedulePhase.SYLLABUS_COMPLETION),

        // 12 Nov
        ScheduleTaskEntity("TASK_20261112_1", "2026-11-12", "4:00 PM – 6:00 PM", "AEC", "Ability Enhancement Course", "AEC_U3", "AEC Topic 3", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261112_2", "2026-11-12", "6:30 PM – 8:30 PM", "SEC", "Skill Enhancement Course", "SEC_U3", "SEC Topic 3", SchedulePhase.SYLLABUS_COMPLETION),

        // 13 Nov
        ScheduleTaskEntity("TASK_20261113_1", "2026-11-13", "4:00 PM – 6:00 PM", "VAC", "Value Addition Course", "VAC_U3", "VAC Topic 3", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261113_2", "2026-11-13", "6:30 PM – 8:30 PM", "FA", "Financial Accounting", "FA_U4", "Financial Accounting Unit 4", SchedulePhase.SYLLABUS_COMPLETION),

        // 14 Nov
        ScheduleTaskEntity("TASK_20261114_1", "2026-11-14", "4:00 PM – 6:00 PM", "BL", "Business Laws", "BL_U4", "Business Laws Unit 4", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261114_2", "2026-11-14", "6:30 PM – 8:30 PM", "MPA", "Management Principles & Applications", "MPA_U4", "MPA Unit 4", SchedulePhase.SYLLABUS_COMPLETION),

        // 15 Nov
        ScheduleTaskEntity("TASK_20261115_1", "2026-11-15", "4:00 PM – 6:00 PM", "GE", "Generic Elective (GE)", "GE_U4", "GE Topic 4", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261115_2", "2026-11-15", "6:30 PM – 8:30 PM", "AEC", "Ability Enhancement Course", "AEC_U4", "AEC Topic 4", SchedulePhase.SYLLABUS_COMPLETION),

        // 16 Nov
        ScheduleTaskEntity("TASK_20261116_1", "2026-11-16", "4:00 PM – 6:00 PM", "SEC", "Skill Enhancement Course", "SEC_U4", "SEC Topic 4", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261116_2", "2026-11-16", "6:30 PM – 8:30 PM", "VAC", "Value Addition Course", "VAC_U4", "VAC Topic 4", SchedulePhase.SYLLABUS_COMPLETION),

        // 17 Nov
        ScheduleTaskEntity("TASK_20261117_1", "2026-11-17", "4:00 PM – 6:00 PM", "FA", "Financial Accounting", "FA_U5", "Financial Accounting Unit 5", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261117_2", "2026-11-17", "6:30 PM – 8:30 PM", "BL", "Business Laws", "BL_U5", "Business Laws Unit 5", SchedulePhase.SYLLABUS_COMPLETION),

        // 18 Nov
        ScheduleTaskEntity("TASK_20261118_1", "2026-11-18", "4:00 PM – 6:00 PM", "MPA", "Management Principles & Applications", "MPA_U5", "MPA Unit 5", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261118_2", "2026-11-18", "6:30 PM – 8:30 PM", "GE", "Generic Elective (GE)", "GE_U5", "GE Topic 5", SchedulePhase.SYLLABUS_COMPLETION),

        // 19 Nov
        ScheduleTaskEntity("TASK_20261119_1", "2026-11-19", "4:00 PM – 6:00 PM", "AEC", "Ability Enhancement Course", "AEC_U5", "AEC Topic 5", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261119_2", "2026-11-19", "6:30 PM – 8:30 PM", "SEC", "Skill Enhancement Course", "SEC_U5", "SEC Topic 5", SchedulePhase.SYLLABUS_COMPLETION),

        // 20 Nov
        ScheduleTaskEntity("TASK_20261120_1", "2026-11-20", "4:00 PM – 6:00 PM", "VAC", "Value Addition Course", "VAC_U5", "VAC Topic 5", SchedulePhase.SYLLABUS_COMPLETION),
        ScheduleTaskEntity("TASK_20261120_2", "2026-11-20", "6:30 PM – 8:30 PM", "ALL", "Consolidation & Backlog", "", "Catch-up and pending topics", SchedulePhase.SYLLABUS_COMPLETION),

        // 21 Nov
        ScheduleTaskEntity("TASK_20261121_1", "2026-11-21", "4:00 PM – 6:00 PM", "FA", "Financial Accounting", "", "Financial Accounting practice", SchedulePhase.PRACTICE_MOCK),
        ScheduleTaskEntity("TASK_20261121_2", "2026-11-21", "6:30 PM – 8:30 PM", "BL", "Business Laws", "", "Business Laws practice", SchedulePhase.PRACTICE_MOCK),

        // 22 Nov
        ScheduleTaskEntity("TASK_20261122_1", "2026-11-22", "4:00 PM – 6:00 PM", "MPA", "Management Principles & Applications", "", "MPA practice", SchedulePhase.PRACTICE_MOCK),
        ScheduleTaskEntity("TASK_20261122_2", "2026-11-22", "6:30 PM – 8:30 PM", "GE", "Generic Elective (GE)", "", "GE revision", SchedulePhase.PRACTICE_MOCK),

        // 23 Nov
        ScheduleTaskEntity("TASK_20261123_1", "2026-11-23", "4:00 PM – 6:00 PM", "AEC", "Ability Enhancement Course", "", "AEC revision", SchedulePhase.PRACTICE_MOCK),
        ScheduleTaskEntity("TASK_20261123_2", "2026-11-23", "6:30 PM – 8:30 PM", "SEC", "Skill Enhancement Course", "", "SEC revision", SchedulePhase.PRACTICE_MOCK),

        // 24 Nov
        ScheduleTaskEntity("TASK_20261124_1", "2026-11-24", "4:00 PM – 6:00 PM", "VAC", "Value Addition Course", "", "VAC revision", SchedulePhase.PRACTICE_MOCK),
        ScheduleTaskEntity("TASK_20261124_2", "2026-11-24", "6:30 PM – 8:30 PM", "ALL", "Consolidation & Backlog", "", "Remaining backlog", SchedulePhase.PRACTICE_MOCK),

        // === PHASE 2: FIRST REVISION (25 Nov – 30 Nov) ===
        // 25 Nov
        ScheduleTaskEntity("TASK_20261125_1", "2026-11-25", "4:00 PM – 6:00 PM", "FA", "Financial Accounting", "FA_U1", "Financial Accounting Units 1–2", SchedulePhase.FIRST_REVISION),
        ScheduleTaskEntity("TASK_20261125_2", "2026-11-25", "6:30 PM – 8:30 PM", "BL", "Business Laws", "BL_U1", "Business Laws Units 1–2", SchedulePhase.FIRST_REVISION),

        // 26 Nov
        ScheduleTaskEntity("TASK_20261126_1", "2026-11-26", "4:00 PM – 6:00 PM", "FA", "Financial Accounting", "FA_U3", "Financial Accounting Units 3–5", SchedulePhase.FIRST_REVISION),
        ScheduleTaskEntity("TASK_20261126_2", "2026-11-26", "6:30 PM – 8:30 PM", "BL", "Business Laws", "BL_U3", "Business Laws Units 3–5", SchedulePhase.FIRST_REVISION),

        // 27 Nov
        ScheduleTaskEntity("TASK_20261127_1", "2026-11-27", "4:00 PM – 6:00 PM", "MPA", "Management Principles & Applications", "MPA_U1", "MPA Units 1–3", SchedulePhase.FIRST_REVISION),
        ScheduleTaskEntity("TASK_20261127_2", "2026-11-27", "6:30 PM – 8:30 PM", "MPA", "Management Principles & Applications", "MPA_U4", "MPA Units 4–5", SchedulePhase.FIRST_REVISION),

        // 28 Nov
        ScheduleTaskEntity("TASK_20261128_1", "2026-11-28", "4:00 PM – 6:00 PM", "GE", "Generic Elective (GE)", "GE_U1", "GE complete revision", SchedulePhase.FIRST_REVISION),
        ScheduleTaskEntity("TASK_20261128_2", "2026-11-28", "6:30 PM – 8:30 PM", "AEC", "Ability Enhancement Course", "AEC_U1", "AEC complete revision", SchedulePhase.FIRST_REVISION),

        // 29 Nov
        ScheduleTaskEntity("TASK_20261129_1", "2026-11-29", "4:00 PM – 6:00 PM", "SEC", "Skill Enhancement Course", "SEC_U1", "SEC complete revision", SchedulePhase.FIRST_REVISION),
        ScheduleTaskEntity("TASK_20261129_2", "2026-11-29", "6:30 PM – 8:30 PM", "VAC", "Value Addition Course", "VAC_U1", "VAC complete revision", SchedulePhase.FIRST_REVISION),

        // 30 Nov
        ScheduleTaskEntity("TASK_20261130_1", "2026-11-30", "4:00 PM – 6:00 PM", "ALL", "All Subjects", "", "Weak topics from all subjects", SchedulePhase.FIRST_REVISION),
        ScheduleTaskEntity("TASK_20261130_2", "2026-11-30", "6:30 PM – 8:30 PM", "ALL", "Exam Mock Prep", "", "Important questions and mock practice", SchedulePhase.FIRST_REVISION),

        // === PHASE 3: SECOND REVISION (1 Dec – 5 Dec) ===
        // 1 Dec
        ScheduleTaskEntity("TASK_20261201_1", "2026-12-01", "4:00 PM – 6:00 PM", "FA", "Financial Accounting", "", "Financial Accounting Second Revision", SchedulePhase.SECOND_REVISION),
        ScheduleTaskEntity("TASK_20261201_2", "2026-12-01", "6:30 PM – 8:30 PM", "BL", "Business Laws", "", "Business Laws Second Revision", SchedulePhase.SECOND_REVISION),

        // 2 Dec
        ScheduleTaskEntity("TASK_20261202_1", "2026-12-02", "4:00 PM – 6:00 PM", "MPA", "Management Principles & Applications", "", "MPA Second Revision", SchedulePhase.SECOND_REVISION),
        ScheduleTaskEntity("TASK_20261202_2", "2026-12-02", "6:30 PM – 8:30 PM", "GE", "Generic Elective (GE)", "", "GE Second Revision", SchedulePhase.SECOND_REVISION),

        // 3 Dec
        ScheduleTaskEntity("TASK_20261203_1", "2026-12-03", "4:00 PM – 6:00 PM", "AEC", "Ability Enhancement Course", "", "AEC Second Revision", SchedulePhase.SECOND_REVISION),
        ScheduleTaskEntity("TASK_20261203_2", "2026-12-03", "6:30 PM – 8:30 PM", "SEC", "Skill Enhancement Course", "", "SEC Second Revision", SchedulePhase.SECOND_REVISION),

        // 4 Dec
        ScheduleTaskEntity("TASK_20261204_1", "2026-12-04", "4:00 PM – 6:00 PM", "VAC", "Value Addition Course", "", "VAC Second Revision", SchedulePhase.SECOND_REVISION),
        ScheduleTaskEntity("TASK_20261204_2", "2026-12-04", "6:30 PM – 8:30 PM", "FA", "Financial Accounting", "", "Financial Accounting weak areas", SchedulePhase.SECOND_REVISION),

        // 5 Dec
        ScheduleTaskEntity("TASK_20261205_1", "2026-12-05", "4:00 PM – 6:00 PM", "BL", "Business Laws & MPA", "", "Business Laws and MPA", SchedulePhase.SECOND_REVISION),
        ScheduleTaskEntity("TASK_20261205_2", "2026-12-05", "6:30 PM – 8:30 PM", "ALL", "GE, AEC, SEC & VAC", "", "GE, AEC, SEC and VAC quick revision", SchedulePhase.SECOND_REVISION),

        // === FINAL PREPARATION (6 Dec) ===
        // 6 Dec
        ScheduleTaskEntity("TASK_20261206_1", "2026-12-06", "4:00 PM – 6:00 PM", "FA", "Accounting & Law Provisions", "", "Financial Accounting formulas and numerical concepts; Business Laws provisions", SchedulePhase.FINAL_PREPARATION),
        ScheduleTaskEntity("TASK_20261206_2", "2026-12-06", "6:30 PM – 8:30 PM", "ALL", "Grand Synthesis", "", "MPA definitions and theories; GE, AEC, SEC and VAC quick revision; Final self-assessment", SchedulePhase.FINAL_PREPARATION)
    )

    val alarms = listOf(
        AlarmItemEntity(
            id = 1,
            title = "Wake up and start morning revision",
            timeStr = "04:00 AM",
            hour = 4,
            minute = 0,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Chimes"
        ),
        AlarmItemEntity(
            id = 2,
            title = "Start revision session",
            timeStr = "04:05 AM",
            hour = 4,
            minute = 5,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Gentle Pulse"
        ),
        AlarmItemEntity(
            id = 3,
            title = "Morning revision completed",
            timeStr = "05:00 AM",
            hour = 5,
            minute = 0,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Success Bell"
        ),
        AlarmItemEntity(
            id = 4,
            title = "Main study session begins",
            timeStr = "04:00 PM",
            hour = 16,
            minute = 0,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Command Clarion"
        ),
        AlarmItemEntity(
            id = 5,
            title = "First study session completed",
            timeStr = "06:00 PM",
            hour = 18,
            minute = 0,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Halfway Bell"
        ),
        AlarmItemEntity(
            id = 6,
            title = "Second study session begins",
            timeStr = "06:30 PM",
            hour = 18,
            minute = 30,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Command Clarion"
        ),
        AlarmItemEntity(
            id = 7,
            title = "Second study session completed",
            timeStr = "08:30 PM",
            hour = 20,
            minute = 30,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Halfway Bell"
        ),
        AlarmItemEntity(
            id = 8,
            title = "Practice and self-testing",
            timeStr = "09:00 PM",
            hour = 21,
            minute = 0,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Focus Tone"
        ),
        AlarmItemEntity(
            id = 9,
            title = "Daily study target completed",
            timeStr = "10:00 PM",
            hour = 22,
            minute = 0,
            isEnabled = true,
            isDailyRoutine = true,
            soundName = "Triumph Chime"
        )
    )

    val motivationalQuotes = listOf(
        "Discipline beats motivation.",
        "One unit at a time. One day at a time.",
        "Your competition is yesterday's version of yourself.",
        "Small progress every day creates big results.",
        "Don't break the chain.",
        "Your future is built by what you study today.",
        "Excellence is not an act, but a habit.",
        "Victory belongs to the most persevering."
    )
}
