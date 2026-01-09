import type { Route } from "./+types/home";
import { redirect } from "react-router";

export function meta({}: Route.MetaArgs) {
  return [
    { title: "Chat | AI Assistant" },
    { name: "description", content: "Chat with AI Assistant" },
  ];
}

export async function loader() {
  // Redirect to /chat
  return redirect("/chat");
}

export default function Home() {
  return null;
}
