const apiBase = "";

const contactForm = document.getElementById("contactForm");
const originalMobileNumberInput = document.getElementById("originalMobileNumber");
const mobileNumberInput = document.getElementById("mobileNumber");
const nameInput = document.getElementById("name");
const genderInput = document.getElementById("gender");
const typeInput = document.getElementById("type");
const formTitle = document.getElementById("formTitle");
const submitButton = document.getElementById("submitButton");
const cancelButton = document.getElementById("cancelButton");
const refreshButton = document.getElementById("refreshButton");
const logoutButton = document.getElementById("logoutButton");
const currentUserLabel = document.getElementById("currentUser");
const tableBody = document.getElementById("contactsTableBody");
const emptyState = document.getElementById("emptyState");
const contactCount = document.getElementById("contactCount");
const message = document.getElementById("message");

let contacts = [];
let currentUser = {
    username: "",
    roles: []
};

function hasRole(role) {
    return currentUser.role === `ROLE_${role}`;
}

function canAdd() {
    return hasRole("ADMIN") || hasRole("USER");
}

function canDelete() {
    return hasRole("ADMIN") || hasRole("MANAGER");
}

function canUpdate() {
    return hasRole("USER");
}

logoutButton.addEventListener("click", async () => {
    await fetch("/logout", {
        method: "POST",
        credentials: "same-origin"
    });

    window.location.href = "/login?logout";
});

async function request(path, options = {}) {
    const response = await fetch(`${apiBase}${path}`, {
        ...options,
        credentials: "same-origin",
        headers: {
            ...(options.headers || {})
        }
    });

    if (response.redirected && response.url.includes("/login")) {
        window.location.href = response.url;
        return null;
    }

    if (response.status === 403) {
        throw new Error("You do not have permission to perform this action.");
    }

    if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`);
    }

    const text = await response.text();
    return text ? JSON.parse(text) : null;
}

function showMessage(text, type = "success") {
    message.textContent = text;
    message.className = `message ${type}`;
    message.hidden = false;
}

function clearMessage() {
    message.hidden = true;
    message.textContent = "";
}

function contactFromForm() {
    return {
        mobileNumber: Number(mobileNumberInput.value),
        name: nameInput.value.trim(),
        gender: genderInput.value.trim(),
        type: typeInput.value
    };
}

function resetForm() {
    contactForm.reset();
    originalMobileNumberInput.value = "";
    typeInput.value = "Business";
    formTitle.textContent = "Add Contact";
    submitButton.textContent = "Add Contact";
    cancelButton.hidden = true;

    contactForm.hidden = !canAdd();
}

function renderContacts() {
    tableBody.innerHTML = "";
    emptyState.hidden = contacts.length > 0;
    contactCount.textContent = `${contacts.length} ${contacts.length === 1 ? "contact" : "contacts"}`;

    contacts.forEach((contact) => {
        const editButton = canUpdate()
            ? `<button type="button" data-action="edit" data-mobile="${contact.mobileNumber}">Edit</button>`
            : "";

        const deleteButton = canDelete()
            ? `<button class="danger" type="button" data-action="delete" data-mobile="${contact.mobileNumber}">Delete</button>`
            : "";

        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${contact.mobileNumber}</td>
            <td>${escapeHtml(contact.name)}</td>
            <td>${escapeHtml(contact.gender)}</td>
            <td>${escapeHtml(contact.type)}</td>
            <td class="actions">
                ${editButton}
                ${deleteButton}
            </td>
        `;
        tableBody.appendChild(row);
    });
}

function escapeHtml(value) {
    return String(value ?? "")
        .replaceAll("&", "&amp;")
        .replaceAll("<", "&lt;")
        .replaceAll(">", "&gt;")
        .replaceAll('"', "&quot;")
        .replaceAll("'", "&#039;");
}

async function loadContacts() {
    clearMessage();
    contacts = await request("/contact/getall");
    if (contacts) renderContacts();
}

async function loadCurrentUser() {
    currentUser = await request("/contact/user");

    if (!currentUser.role) {
        currentUser.role = "NO_ROLE";
    }

    currentUserLabel.textContent = `${currentUser.username} (${currentUser.role})`;

    contactForm.hidden = !canAdd();
}

function startEdit(mobileNumber) {
    if (!canUpdate()) {
        showMessage("Only USER role can update contacts.", "error");
        return;
    }

    const contact = contacts.find((item) => item.mobileNumber === mobileNumber);
    if (!contact) return;

    originalMobileNumberInput.value = contact.mobileNumber;
    mobileNumberInput.value = contact.mobileNumber;
    nameInput.value = contact.name;
    genderInput.value = contact.gender;
    typeInput.value = contact.type;
    formTitle.textContent = "Update Contact";
    submitButton.textContent = "Update Contact";
    cancelButton.hidden = false;
}

async function deleteContact(mobileNumber) {
    const deleted = await request(`/contact/delete/${mobileNumber}`, {
        method: "DELETE"
    });

    if (!deleted) throw new Error("Contact was not deleted.");

    showMessage("Contact deleted.");
    await loadContacts();
}

contactForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    try {
        const contact = contactFromForm();
        const originalMobileNumber = originalMobileNumberInput.value;

        if (originalMobileNumber) {
            if (!canUpdate()) {
                throw new Error("Only USER role can update contacts.");
            }

            const updated = await request(`/contact/update/${originalMobileNumber}`, {
                method: "PUT",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(contact)
            });

            if (!updated) throw new Error("Contact was not updated.");

            showMessage("Contact updated.");
        } else {
            if (!canAdd()) {
                throw new Error("Only ADMIN or USER can add contacts.");
            }

            const added = await request("/contact/add", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(contact)
            });

            if (!added) throw new Error("Contact already exists.");

            showMessage("Contact added.");
        }

        resetForm();
        await loadContacts();
    } catch (error) {
        showMessage(error.message, "error");
    }
});

tableBody.addEventListener("click", async (event) => {
    const button = event.target.closest("button");
    if (!button) return;

    const mobileNumber = Number(button.dataset.mobile);

    if (button.dataset.action === "edit") {
        startEdit(mobileNumber);
        return;
    }

    if (button.dataset.action === "delete" && confirm("Delete this contact?")) {
        try {
            await deleteContact(mobileNumber);
        } catch (error) {
            showMessage(error.message, "error");
        }
    }
});

refreshButton.addEventListener("click", async () => {
    try {
        await loadContacts();
        showMessage("Contacts refreshed.");
    } catch (error) {
        showMessage(error.message, "error");
    }
});

cancelButton.addEventListener("click", resetForm);

renderContacts();
loadCurrentUser()
    .then(loadContacts)
    .catch((error) => showMessage(error.message, "error"));