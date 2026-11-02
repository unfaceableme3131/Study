package com.example.ai

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withTimeout

class AiStudyTutor {

    private val model by lazy {
        Firebase.ai.generativeModel(
            modelName = "gemini-3.8-flash",
            generationConfig = generationConfig {
                temperature = 0.7f
                topK = 40
                topP = 0.95f
            }
        )
    }

    private val systemContext = """
        You are the academic AI Copilot and Tutor for a B.Com (Hons.) Semester 1 student.
        Your expertise spans:
        1. Financial Accounting (Indian GAAP, AS-2 Inventory, AS-10 PPE, Sole Proprietorship, NPO, Branch Accounting, CAS)
        2. Business Laws (Indian Contract Act 1872, Sale of Goods Act 1930, LLP Act 2008, landmark case laws)
        3. Management Principles & Applications (MPA - Taylor, Fayol, MBO, Planning, Organising, Directing, Control)
        4. Generic Elective (GE), AEC, SEC, and VAC courses.
        Provide structured, clear, high-scoring answers with:
        - Key concept definitions
        - Bullet points & statutory provisions / formulas
        - Landmark case law citations (for Business Laws)
        - Exam presentation tips
        Be encouraging, rigorous, and direct.
    """.trimIndent()

    fun streamAnswer(subject: String, userQuestion: String): Flow<String> = flow {
        val fullPrompt = "$systemContext\n\nSubject: $subject\nQuestion/Doubt: $userQuestion\n\nAnswer:"
        try {
            withTimeout(60_000L) {
                model.generateContentStream(fullPrompt).collect { chunk ->
                    val chunkText = chunk.text.orEmpty()
                    if (chunkText.isNotEmpty()) {
                        emit(chunkText)
                    }
                }
            }
        } catch (e: TimeoutCancellationException) {
            emit("\n[Timed out after 60s. Please retry your question.]")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Log.e("AiStudyTutor", "Gemini stream error", e)
            emit("\n[AI response failed: ${e.localizedMessage ?: "Unknown error"}. Check network or App Check.]")
        }
    }

    fun generateQuickSummary(subject: String, topic: String): Flow<String> = flow {
        val prompt = "$systemContext\n\nCreate a high-yield, quick revision cheat sheet for '$topic' in '$subject'. Include definitions, formulas/provisions, 3 key exam points, and common student mistakes."
        try {
            withTimeout(60_000L) {
                model.generateContentStream(prompt).collect { chunk ->
                    emit(chunk.text.orEmpty())
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emit("Failed to generate summary: ${e.localizedMessage}")
        }
    }

    fun generateMockQuestions(subject: String, unit: String): Flow<String> = flow {
        val prompt = "$systemContext\n\nGenerate 3 realistic B.Com (Hons.) university exam questions for '$unit' in '$subject':\n1. One 5-mark short conceptual question.\n2. One 10-mark practical problem or case law scenario.\n3. Model answer hints & key points."
        try {
            withTimeout(60_000L) {
                model.generateContentStream(prompt).collect { chunk ->
                    emit(chunk.text.orEmpty())
                }
            }
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            emit("Failed to generate mock questions: ${e.localizedMessage}")
        }
    }
}
